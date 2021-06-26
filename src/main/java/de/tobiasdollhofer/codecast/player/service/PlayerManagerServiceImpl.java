package de.tobiasdollhofer.codecast.player.service;

import com.google.common.collect.Iterables;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.OpenSourceUtil;
import com.intellij.util.PsiNavigateUtil;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.*;
import de.tobiasdollhofer.codecast.player.util.event.*;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEvent;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.ui.PlayerUI;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class PlayerManagerServiceImpl implements PlayerManagerService, Notifiable {

    private Playlist playlist;
    private final Project project;
    private AudioComment comment;
    private final CommentPlayer player;
    private final PlayerUI ui;
    private boolean playing;
    private boolean autoPlayback = false;
    private boolean jumpToCode = false;

    public PlayerManagerServiceImpl(Project project) {
        this.project = project;
        this.player = new CommentPlayer();
        this.ui = new PlayerUI(project);
        this.playing = false;
        addListeners();
    }

    /**
     * adds PlayerManagerServiceImpl instance to the player and ui for event handling
     */
    private void addListeners() {
        player.addListener(this);
        ui.addListener(this);
    }

    /**
     * notifies the PlayerManagerServiceImpl about specific events (i.e. UI-Actions, Player-Actions and Download-Actions)
     * @param e event to handle
     */
    @Override
    public void notify(Event e) {
        System.out.println("Notified: " + e.toString());
        if(e.getClass().equals(PlayerEvent.class)){
            notifyPlayerEvent((PlayerEvent) e);
        }else if(e.getClass().equals(UIEvent.class)){
            notifyUIEvent((UIEvent) e);
        }else if(e.getClass().equals(DownloadEvent.class)){
            notifyDownloadEvent((DownloadEvent) e);
        }
    }

    /**
     * routes UI-Event based on its type
     * triggers actions on player and sometimes even on UI
     * @param e UI event to handle
     */
    private void notifyUIEvent(UIEvent e) {
        switch(e.getType()){
            case PLAY_FIRST_CLICKED:
                playFirstClicked();
                break;

            case PLAY_PREVIOUS_CLICKED:
                playPreviousClicked();
                break;

            case PLAY_PAUSE_CLICKED:
                playPauseClicked();
                break;

            case PLAY_NEXT_CLICKED:
                playNextClicked();
                break;

            case PLAY_LAST_CLICKED:
                playLastClicked();
                break;

            case VOLUME_CHANGE:
                volumeChanged(Double.parseDouble(e.getData()));
                break;

            case LIST_CLICKED:
                listClicked();
                break;

            case RESET_PLAYER:
                resetPlayer();
                break;

            case AUTOPLAY_CLICKED:
                autoplayClicked();
                break;

            case JUMP_TO_CODE_CLICKED:
                jumpToCodeClicked();
                break;

            case SHOW_CODE_CLICKED:
                showCodeClicked();
                break;

            case PROGRESSBAR_CLICKED:
                onProgressBarClicked(Double.parseDouble(e.getData()));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }


    }


    /**
     * sets first comment of playlist as current
     */
    private void playFirstClicked() {
        AudioComment comment = playlist.getFirstComment();
        setComment(comment);
    }

    /**
     * sets previous comment of playlist if current isn't the first one
     */
    private void playPreviousClicked() {
        AudioComment comment = playlist.getPreviousComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    /**
     * stops/starts player
     */
    private void playPauseClicked() {
        if(playing){
            player.pause();
        }else{
            player.play();
        }
    }

    /**
     * sets next comment if current isn't the last one
     */
    private void playNextClicked() {
        AudioComment comment = playlist.getNextComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    /**
     * sets last comment of playlist as current
     */
    private void playLastClicked() {
        AudioComment comment = playlist.getLastComment();
        setComment(comment);
    }

    /**
     * sets volume of player
     * @param val value between 0 and 1 for volume
     */
    private void volumeChanged(double val) {
        player.setVolume(val);
    }

    /**
     * sets selected comment from list for the player
     */
    private void listClicked() {
        setComment(ui.getSelectedListComment());
    }

    /**
     * reloads playlist
     * comment will be set after eventual download is finished -> onDownloadFinished()
     */
    private void resetPlayer() {
        player.pause();
        this.project.getService(PlaylistService.class).loadPlaylist();
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
    }

    /**
     * stores the autoplaystatus in PlayerManagerServiceImpl to handle the end of playback correctly
     */
    private void autoplayClicked() {
        autoPlayback = ui.getAutoplayStatus();
    }


    /**
     * stores the jump-to-code status to handle playback actions correctly
     */
    private void jumpToCodeClicked() {
        jumpToCode = ui.getJumpToCodeStatus();
    }

    /**
     * jumps to code position of comment
     */
    private void showCodeClicked() {
        JumpToCodeUtil.jumpToCode(comment);
    }

    /**
     * percentage position of the comment
     * @param position double value between 0 and 1
     */
    private void onProgressBarClicked(double position) {
        Duration duration = comment.getDuration();
        if(duration != null){
            int seconds = (int)comment.getDuration().toSeconds();
            player.goToPosition( (int)(seconds * position));
        }
    }

    /**
     * routes Player-Event based on its type
     * triggers actions on ui
     * @param e Player event to handle
     */
    private void notifyPlayerEvent(PlayerEvent e) {
        switch (e.getType()){
            case INITIALIZED:
                onPlayerInitialized();
                break;

            case STARTED:
                onPlayerStarted();
                break;

            case STOPPED:
                onPlayerStopped();
                break;

            case ENDED:
                onPlayerEnded();
                break;

            case PROGRESS_CHANGED:
                onProgressChanged();
                break;

            case MEDIA_UNAVAILABLE:
                onMediaUnavailable();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }
    }

    /**
     * sets ui progressbar and value to 0:00/x:xx
     */
    private void onPlayerInitialized() {
        onProgressChanged();
        ui.setComment(this.comment);
    }

    /**
     * changes player icon in ui
     */
    private void onPlayerStarted() {
        this.playing = true;
        this.ui.playPlayer();
    }

    /**
     * cchanges player icon in ui
     */
    private void onPlayerStopped() {
        this.playing = false;
        this.ui.pausePlayer();
    }

    /**
     * resets current comment or plays next comment depending on auto play setting
     */
    private void onPlayerEnded() {
        this.ui.pausePlayer();
        if(this.autoPlayback){
            AudioComment comment = playlist.getNextComment(this.comment);
            if(comment != null)
                setComment(comment);
        }else{
            this.playing = false;
        }
        onProgressChanged();
    }

    /**
     * sets progressbar and progress time of ui
     */
    private void onProgressChanged() {
        this.ui.setProgress(this.player.getProgressPercentage());
        this.ui.setProgressTime(DurationFormatter.formatDuration(this.player.getDurationProgress())
                + "/"
                + DurationFormatter.formatDuration(comment.getDuration()));
    }

    /**
     * shows message if comment file isn' available
     */
    private void onMediaUnavailable() {
        BalloonNotifier.notifyError(this.project, "Current file with path " + FilePathUtil.getFilePathForComment(this.project, comment) + " not available!");
    }

    /**
     * routes Download-Event based on its type
     * triggers actions on ui and player
     * @param e Download event to handle
     */
    private void notifyDownloadEvent(DownloadEvent e) {
        switch(e.getType()){
            case FINISHED:
                onDownloadFinished();
                break;

            case CANCELED:
                onDownloadCanceled();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }
    }

    /**
     * set first comment and initialize ui list view
     */
    private void onDownloadFinished() {
        playlist = project.getService(PlaylistService.class).getPlaylist();
        if(playlist != null && !playlist.isEmpty()){
            ui.setPlaylist(playlist);
            ui.enablePlayer(true);
            comment = playlist.getFirstComment();
            setComment(comment);
        }else{
            ui.enablePlayer(false);
        }
    }

    /**
     * show error message
     */
    private void onDownloadCanceled() {
        BalloonNotifier.notifyError(project, "Download was canceled. Please reset player.");
        ui.enablePlayer(false);
    }

    /**
     * sets comment as current comment if file is already downloaded
     * @param comment
     */
    private void setComment(AudioComment comment){
        // store current play state temporarily, because play state will be set to false when player will be paused
        // playingTemp is used for restart playback some lines below
        boolean playingTemp = this.playing;
        player.pause();
        if(comment != null && FilePathUtil.checkCommentDownloaded(project, comment)){
            this.comment = comment;
            // jump to code position if it is activated
            if(jumpToCode){
                JumpToCodeUtil.jumpToCode(comment);
            }
            player.setPath(FilePathUtil.getFilePathForCommentWithPrefix(project, comment), playingTemp);
        }
    }


    public boolean isAutoPlayback() {
        return autoPlayback;
    }

    public void setAutoPlayback(boolean autoPlayback) {
        this.autoPlayback = autoPlayback;
    }

    public void setPlaylistCommentForFoundComment(AudioComment comment){
        for(AudioComment playlistComment : playlist.getAllComments()){
            if(playlistComment.equals(comment)){
                if(!playing){
                    playing = true;
                }
                setComment(playlistComment);
            }
        }
    }
    /**
     * returns ui for factory
     * @return ui
     */
    @Override
    public PlayerUI getPlayerUI() {
        return ui;
    }
}
