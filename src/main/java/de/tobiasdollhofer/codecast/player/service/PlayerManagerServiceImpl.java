package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.event.*;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.ui.PlayerUI;

@Service
public class PlayerManagerServiceImpl implements PlayerManagerService, Notifiable {

    /**
     * TODO:
     * BUG: Play-Pause-Icon is wrong
     * BUG: Player won't be paused when next comment is selected
     * FEATURE: Progress displaying
     */
    private Playlist playlist;
    private final Project project;
    private AudioComment comment;
    private CommentPlayer player;
    private PlayerUI ui;
    private boolean playing;
    private boolean autoPlayback = false;

    public PlayerManagerServiceImpl(Project project) {
        this.project = project;
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
        this.player = new CommentPlayer();
        this.ui = new PlayerUI(project);
        this.playing = false;
        if(playlist != null){
            this.comment = playlist.getFirstComment();
            setComment(this.comment);
        }
        addListeners();
    }

    private void addListeners() {
        player.addListener(this);
        ui.addListener(this);
    }

    @Override
    public void notify(Event e) {
        System.out.println("Notified: " + e.toString());
        if(e.getClass().equals(PlayerEvent.class)){
            notifyPlayerEvent((PlayerEvent) e);
        }else if(e.getClass().equals(UIEvent.class)){
            notifyUIEvent((UIEvent) e);
        }
    }

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

                break;

            case RESET_PLAYER:
                resetPlayer();
                break;

            case AUTOPLAY_CLICKED:
                autoplayClicked();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }


    }


    private void playFirstClicked() {
        AudioComment comment = playlist.getFirstComment();
        setComment(comment);
    }

    private void playPreviousClicked() {
        AudioComment comment = playlist.getPreviousComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    private void playPauseClicked() {
        if(playing){
            player.pause();
        }else{
            player.play();
        }
        //playing = !playing;
        System.out.println(playing);
    }

    private void playNextClicked() {
        AudioComment comment = playlist.getNextComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    private void playLastClicked() {
        AudioComment comment = playlist.getLastComment();
        setComment(comment);
    }

    private void volumeChanged(double val) {
        player.setVolume(val);
    }

    private void resetPlayer() {
        player.pause();
        this.project.getService(PlaylistService.class).loadPlaylist();
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
        if(this.playlist != null){
            ui.enablePlayer(true);
            setComment(this.playlist.getFirstComment());
        }else{
            ui.enablePlayer(false);
        }
    }

    private void autoplayClicked() {
        autoPlayback = ui.getAutoplayStatus();
    }


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

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }
    }

    private void onPlayerInitialized() {
        onProgressChanged();
    }

    private void onPlayerStarted() {
        this.playing = true;
        this.ui.playPlayer();
    }

    private void onPlayerStopped() {
        this.playing = false;
        this.ui.pausePlayer();
    }

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

    private void onProgressChanged() {
        this.ui.setProgress(this.player.getProgressPercentage());
        this.ui.setProgressTime(this.player.getFormattedProgress());
    }

    private void setComment(AudioComment comment){
        // store current play state temporarily, because play state will be set to false when player will be paused
        // playingTemp is used for restart playback some lines below
        boolean playingTemp = this.playing;
        player.pause();
        this.comment = comment;
        ui.setComment(this.comment);
        if(comment != null){
            player.setPath("file:///" + FilePathUtil.getCodeCastAudioDirectory(project) + this.comment.getPath(), playingTemp);
        }
    }


    public boolean isAutoPlayback() {
        return autoPlayback;
    }

    public void setAutoPlayback(boolean autoPlayback) {
        this.autoPlayback = autoPlayback;
    }

    @Override
    public PlayerUI getPlayerUI() {
        return ui;
    }
}
