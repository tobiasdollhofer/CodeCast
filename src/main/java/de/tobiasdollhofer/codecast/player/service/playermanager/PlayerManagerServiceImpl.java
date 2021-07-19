package de.tobiasdollhofer.codecast.player.service.playermanager;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.playlist.PlaylistService;
import de.tobiasdollhofer.codecast.player.util.*;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import de.tobiasdollhofer.codecast.player.util.event.*;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEvent;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.ui.PlayerUI;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;
import de.tobiasdollhofer.codecast.player.util.logging.Context;
import de.tobiasdollhofer.codecast.player.util.logging.CsvLogger;
import de.tobiasdollhofer.codecast.player.util.notification.BalloonNotifier;
import javafx.util.Duration;

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

            case LIST_CLICKED_SAME:
                listClickedSame();
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
        CsvLogger.log(Context.PLAYER, UIEventType.PLAY_FIRST_CLICKED, comment.getTitle());
        setComment(comment);
    }

    /**
     * sets previous comment of playlist if current isn't the first one
     */
    private void playPreviousClicked() {
        AudioComment comment = playlist.getPreviousComment(this.comment);

        if(comment != null)
            CsvLogger.log(Context.PLAYER, UIEventType.PLAY_PREVIOUS_CLICKED, comment.getTitle());
            setComment(comment);
    }

    /**
     * stops/starts player
     */
    private void playPauseClicked() {
        if(playing){
            player.pause();
            CsvLogger.log(Context.PLAYER, UIEventType.PLAY_PAUSE_CLICKED, comment.getTitle() + " paused: " + getFormattedDurationProgress());
        }else{
            player.play();
            CsvLogger.log(Context.PLAYER, UIEventType.PLAY_PAUSE_CLICKED, comment.getTitle() + " started");
        }
    }

    /**
     * sets next comment if current isn't the last one
     */
    private void playNextClicked() {
        AudioComment comment = playlist.getNextComment(this.comment);

        if(comment != null)
            CsvLogger.log(Context.PLAYER, UIEventType.PLAY_NEXT_CLICKED, comment.getTitle());
            setComment(comment);
    }

    /**
     * sets last comment of playlist as current
     */
    private void playLastClicked() {
        AudioComment comment = playlist.getLastComment();
        setComment(comment);
        CsvLogger.log(Context.PLAYER, UIEventType.PLAY_LAST_CLICKED, comment.getTitle());
    }

    /**
     * sets volume of player
     * @param val value between 0 and 1 for volume
     */
    private void volumeChanged(double val) {
        player.setVolume(val);
        CsvLogger.log(Context.PLAYER, UIEventType.VOLUME_CHANGE, String.valueOf(val));
    }

    /**
     * sets selected comment from list for the player
     */
    private void listClicked() {
        setComment(ui.getSelectedListComment());
        CsvLogger.log(Context.PLAYER, UIEventType.LIST_CLICKED, ui.getSelectedListComment().getTitle());
    }

    /**
     * starts playing or pausing if current title was clicked again in list
     */
    private void listClickedSame() {
        if(playing){
            player.pause();
            CsvLogger.log(Context.PLAYER, UIEventType.LIST_CLICKED_SAME, comment.getTitle() + " paused: " + getFormattedDurationProgress());
        }else{
            player.play();
            CsvLogger.log(Context.PLAYER, UIEventType.LIST_CLICKED_SAME, comment.getTitle() + " started");
        }
    }

    /**
     * reloads playlist
     * comment will be set after eventual download is finished -> onDownloadFinished()
     */
    private void resetPlayer() {
        player.pause();
        ui.enablePlayer(false);
        this.project.getService(PlaylistService.class).loadPlaylist();
        CsvLogger.log(Context.PLAYER, UIEventType.RESET_PLAYER, "");
    }

    /**
     * stores the autoplaystatus in PlayerManagerServiceImpl to handle the end of playback correctly
     */
    private void autoplayClicked() {
        autoPlayback = ui.getAutoplayStatus();
        CsvLogger.log(Context.PLAYER, UIEventType.AUTOPLAY_CLICKED, "enabled: " + ui.getAutoplayStatus());
    }


    /**
     * stores the jump-to-code status to handle playback actions correctly
     */
    private void jumpToCodeClicked() {
        jumpToCode = ui.getJumpToCodeStatus();
        CsvLogger.log(Context.PLAYER, UIEventType.JUMP_TO_CODE_CLICKED, "enabled: " + ui.getJumpToCodeStatus());
    }

    /**
     * jumps to code position of comment
     */
    private void showCodeClicked() {
        JumpToCodeUtil.jumpToCode(project, comment);
        CsvLogger.log(Context.PLAYER, UIEventType.SHOW_CODE_CLICKED, comment.getTitle());
    }

    /**
     * percentage position of the comment
     * @param position double value between 0 and 1
     */
    private void onProgressBarClicked(double position) {
        Duration duration = comment.getDuration();
        CsvLogger.log(Context.PLAYER, UIEventType.PROGRESSBAR_CLICKED, "percentage: " + position);
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
        CsvLogger.log(Context.PLAYER, UIEventType.PLAYBACK_ENDED, comment.getTitle());
        if(this.autoPlayback){
            handleAutoPlayback();
        }else{
            this.playing = false;
            onProgressChanged();
        }
    }

    /**
     * sets progressbar and progress time of ui
     */
    private void onProgressChanged() {
        this.ui.setProgress(this.player.getProgressPercentage());
        this.ui.setProgressTime(getFormattedDurationProgress());
    }

    /**
     * shows message if comment file isn' available
     */
    private void onMediaUnavailable() {
        BalloonNotifier.notifyError(this.project, Strings.FILE_NOT_AVAILABLE + FilePathUtil.getFilePathForComment(this.project, comment));
    }


    /**
     * starts new thread to wait a second before setting next comment for playback
     */
    private void handleAutoPlayback() {
        Thread delay = new Thread(() ->{
            try {
                Thread.sleep(1000);
                AudioComment comment = playlist.getNextComment(this.comment);
                if(comment != null)
                    setComment(comment);
                onProgressChanged();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        delay.start();
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
        BalloonNotifier.notifyError(project, Strings.DOWNLOAD_CANCELED);
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
                JumpToCodeUtil.jumpToCode(project, comment);
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


    private String getFormattedDurationProgress(){
        return DurationFormatter.formatDuration(this.player.getDurationProgress())
                + "/"
                + DurationFormatter.formatDuration(comment.getDuration());
    }
    /**
     * Method sets existing comment which is equal to provided comment
     * used i.e. for jump-to-code where comment is extracted from code but is a new entity
     */
    public void setPlaylistCommentForFoundComment(AudioComment found){
        CsvLogger.log(Context.PLAYER, UIEventType.GUTTER_ICON_CLICKED, found.getTitle());
        for(AudioComment playlistComment : playlist.getAllComments()){
            if(playlistComment.equals(found)){
                // only set and play comment if it isn't the current comment, otherwise handle like pause/play control
                if(!this.comment.equals(found)){
                    player.play();
                    setComment(playlistComment);
                }else{
                    playPauseClicked();
                }

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
