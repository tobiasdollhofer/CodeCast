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
    private JToggleButton autoplayButton;

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
        double volume = volumeSlider.getValue();

        if(volumeSlider.getValue() == 0){
            volumeIcon.setIcon(PluginIcons.volumeOff);
        }else{
            volumeIcon.setIcon(PluginIcons.volume);
            volume = volume / 100;
        }
        notifyAll(new UIEvent(VOLUME_CHANGE, String.valueOf(volume)));
    }

    private void playLastClicked() {
        notifyAll(new UIEvent(PLAY_LAST_CLICKED, ""));
    }

    private void playNextClicked() {
        notifyAll(new UIEvent(PLAY_NEXT_CLICKED, ""));
    }

    private void playPauseClicked() {
        notifyAll(new UIEvent(PLAY_PAUSE_CLICKED, ""));
    }

    //TODO: rename
    public void pausePlayer(){
        playPause.setIcon(PluginIcons.play);
    }

    public void playPlayer(){
        playPause.setIcon(PluginIcons.pause);
    }

    public void setProgressTime(String time){
        if(!time.equals(progressTime.getText()))
            progressTime.setText(time);
    }

    public void setProgress(int progress){
        playerProgressBar.setValue(progress);
    }

    private void playPreviousClicked() {
        notifyAll(new UIEvent(PLAY_PREVIOUS_CLICKED, ""));
        //TODO: add some cooldown to restart current comment
    }

    private void playFirstClicked() {
        notifyAll(new UIEvent(PLAY_FIRST_CLICKED, ""));
    }

    private void initToolbarListener() {
        autoplayButton.setIcon(PluginIcons.autoPlay);
        autoplayButton.addActionListener(e -> autoplayButtonClicked());
        reloadButton.addActionListener(e -> reloadPlayer());
    }

    private void autoplayButtonClicked() {
        if(autoplayButton.isSelected()){
            autoplayButton.setText("Autoplay ON");
        }else{
            autoplayButton.setText("Autoplay OFF");
        }
        notifyAll(new UIEvent(AUTOPLAY_CLICKED, String.valueOf(autoplayButton.isSelected())));
    }

    public boolean getAutoplayStatus(){
        return autoplayButton.isSelected();
    }

    private void reloadPlayer() {
        notifyAll(new UIEvent(UIEventType.RESET_PLAYER, "Reloaded player"));
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
