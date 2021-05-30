package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.PlaylistService;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;

import javax.swing.*;

import static de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType.*;

public class PlayerUI extends Observable{

    private JPanel playerWindowContent;
    private JScrollPane playlistPane;
    private JPanel playerPanel;
    private JTextPane currentTitleLabel;
    private JToolBar toolbar;
    private JButton reloadButton;
    private JList playlistList;
    private JTextPane progressTime;
    private JProgressBar playerProgressBar;
    private JButton playFirst;
    private JButton playPrevious;
    private JButton playPause;
    private JButton playNext;
    private JButton playLast;
    private JSlider volumeSlider;
    private JLabel volumeIcon;

    private boolean playing = false;
    private Playlist playlist;
    private AudioComment comment;
   // private CommentPlayer player;
    public String playerTest;

    private final Project project;

    public PlayerUI(Project project){
        super();
        this.project = project;
        //this.playlist = project.getService(PlaylistService.class).getPlaylist();
     //   this.player = new CommentPlayer();
        //if(this.playlist != null){
        //    setComment(this.playlist.getFirstComment());
        //}else{
        //    enablePlayer(false);
            // TODO: ALERT NO XML!!
        //}

        initList();
        initToolbarListener();
        initPlayerControls();
    }

    private void initPlayerControls() {
        initPlayerControlIcons();
        initPlayerControlListener();
    }

    private void initPlayerControlIcons() {
        playFirst.setIcon(PluginIcons.playFirst);
        playPrevious.setIcon(PluginIcons.playPrevious);
        playPause.setIcon(PluginIcons.play);
        playNext.setIcon(PluginIcons.playNext);
        playLast.setIcon(PluginIcons.playLast);
        volumeIcon.setIcon(PluginIcons.volume);
    }

    private void initPlayerControlListener() {
        playFirst.addActionListener(e -> playFirstClicked());
        playPrevious.addActionListener(e -> playPreviousClicked());
        playPause.addActionListener(e -> playPauseClicked());
        playNext.addActionListener(e -> playNextClicked());
        playLast.addActionListener(e -> playLastClicked());
        volumeSlider.addChangeListener(e -> volumeSliderChange());
    }

    private void volumeSliderChange() {
        System.out.println("New Volume: " + volumeSlider.getValue());
        double volume = volumeSlider.getValue();

        if(volumeSlider.getValue() == 0){
            volumeIcon.setIcon(PluginIcons.volumeOff);
        }else{
            volumeIcon.setIcon(PluginIcons.volume);
            volume = volume / 100;
        }
       // player.setVolume(volume);
        notifyAll(new UIEvent(VOLUME_CHANGE, String.valueOf(volume)));
    }

    private void playLastClicked() {
        System.out.println("Play Last clicked!");
        notifyAll(new UIEvent(PLAY_LAST_CLICKED, ""));
        //pausePlayer();
        //AudioComment comment = playlist.getLastComment();

       // if(comment != null)
        //    setComment(comment);
    }

    private void playNextClicked() {
        System.out.println("Play Next clicked!");
        notifyAll(new UIEvent(PLAY_NEXT_CLICKED, ""));
        //pausePlayer();
        //AudioComment comment = playlist.getNextComment(this.comment);

        //if(comment != null)
          //  setComment(comment);
    }

    private void playPauseClicked() {
        System.out.println("Play Pause clicked!");
        notifyAll(new UIEvent(PLAY_PAUSE_CLICKED, ""));
        //if(playing){
          //  pausePlayer();
        //}else{
          //  playPlayer();
        //}
        //System.out.println("CodeCast-Player State: " + this.playing);
    }

    //TODO: rename
    public void pausePlayer(){
        playPause.setIcon(PluginIcons.play);
        //player.pause();
        //playing = false;
    }

    public void playPlayer(){
        playPause.setIcon(PluginIcons.pause);
        //player.run();
        //playing = true;
    }

    public void setProgressTime(String time){
        progressTime.setText(time);
    }

    public void setProgress(int progress){
        playerProgressBar.setValue(progress);
    }

    private void playPreviousClicked() {
        System.out.println("Play Previous clicked!");
        notifyAll(new UIEvent(PLAY_PREVIOUS_CLICKED, ""));
        //TODO: add some cooldown to restart current comment
        //AudioComment comment = playlist.getPreviousComment(this.comment);

        //if(comment != null)
          //  setComment(comment);
    }

    private void playFirstClicked() {
        System.out.println("Play First clicked!");
        notifyAll(new UIEvent(PLAY_FIRST_CLICKED, ""));
        //pausePlayer();
        //AudioComment comment = playlist.getFirstComment();

        //if(comment != null)
          //  setComment(comment);
    }

    private void initToolbarListener() {
        reloadButton.addActionListener(e -> reloadPlayer());
    }

    private void reloadPlayer() {
        System.out.println("Reload Player");
        notifyAll(new UIEvent(UIEventType.RESET_PLAYER, "Reloaded player"));
       // pausePlayer();
        //project.getService(PlaylistService.class).loadPlaylist();
        //this.playlist = project.getService(PlaylistService.class).getPlaylist();
        //if(this.playlist != null){
          //  setComment(this.playlist.getFirstComment());
        //}

    }

    private void initList(){
    }

    public void setComment(AudioComment comment){
        //pausePlayer();
        playerProgressBar.setValue(0);
        if(comment != null){
            this.comment = comment;
            currentTitleLabel.setText(comment.getTitle());
            enablePlayer(true);
        }else {
            currentTitleLabel.setText("No comment available.");
            enablePlayer(false);
        }
        // reinitialize player with stored comment as the resetting of a comment should also reset the player
        //player.setPath("file:///" + FilePathUtil.getCodeCastAudioDirectory(this.project) + this.comment.getPath());
        //System.out.println("Length: " + String.valueOf(player.getLength()));
    }

    public void enablePlayer(boolean enabled){
        this.playFirst.setEnabled(enabled);
        this.playPrevious.setEnabled(enabled);
        this.playPause.setIcon(PluginIcons.play);
        this.playPause.setEnabled(enabled);
        this.playNext.setEnabled(enabled);
        this.playLast.setEnabled(enabled);
        this.volumeSlider.setEnabled(enabled);
        this.playerProgressBar.setEnabled(enabled);
    }

    public JPanel getContent(){
        return playerWindowContent;
    }

}
