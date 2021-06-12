package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
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
    public String playerTest;

    private final Project project;

    public PlayerUI(Project project){
        super();
        this.project = project;
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

    /**
     * notifies all observer about volume change
     */
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

    /**
     * notifies all observer about click event
     */
    private void playLastClicked() {
        notifyAll(new UIEvent(PLAY_LAST_CLICKED, ""));
    }

    /**
     * notifies all observer about click event
     */
    private void playNextClicked() {
        notifyAll(new UIEvent(PLAY_NEXT_CLICKED, ""));
    }

    /**
     * notifies all observer about click event
     */
    private void playPauseClicked() {
        notifyAll(new UIEvent(PLAY_PAUSE_CLICKED, ""));
    }

    /**
     * sets play icon to button
     */
    public void pausePlayer(){
        playPause.setIcon(PluginIcons.play);
    }

    /**
     * sets pause icon to button
     */
    public void playPlayer(){
        playPause.setIcon(PluginIcons.pause);
    }

    /**
     * display current time progress
     * @param time string value of time ( x:xx/x:xx)
     */
    public void setProgressTime(String time){
        if(!time.equals(progressTime.getText()))
            progressTime.setText(time);
    }

    /**
     * sets progressbar progress
     * @param progress int value between 0 and 100
     */
    public void setProgress(int progress){
        playerProgressBar.setValue(progress);
    }

    /**
     * notifies all observer about click event
     */
    private void playPreviousClicked() {
        notifyAll(new UIEvent(PLAY_PREVIOUS_CLICKED, ""));
        //TODO: add some cooldown to restart current comment
    }

    /**
     * notifies all observer about click event
     */
    private void playFirstClicked() {
        notifyAll(new UIEvent(PLAY_FIRST_CLICKED, ""));
    }

    /**
     * inits toolbar buttons
     */
    private void initToolbarListener() {
        autoplayButton.setIcon(PluginIcons.autoPlay);
        autoplayButton.addActionListener(e -> autoplayButtonClicked());
        reloadButton.addActionListener(e -> reloadPlayer());
    }

    /**
     * notifies all observer about click event and sets text to ON/OFF
     */
    private void autoplayButtonClicked() {
        if(autoplayButton.isSelected()){
            autoplayButton.setText("Autoplay ON");
        }else{
            autoplayButton.setText("Autoplay OFF");
        }
        notifyAll(new UIEvent(AUTOPLAY_CLICKED, String.valueOf(autoplayButton.isSelected())));
    }

    /**
     *
     * @return if autoplaybutton is selected
     */
    public boolean getAutoplayStatus(){
        return autoplayButton.isSelected();
    }

    /**
     * notifies all observer about click event
     */
    private void reloadPlayer() {
        notifyAll(new UIEvent(UIEventType.RESET_PLAYER, "Reloaded player"));
    }

    /**
     * TODO
     * @param playlist
     */
    public void setPlaylist(Playlist playlist) {
        System.out.println("SET PLAYLIST");
    }

    /**
     * sets progress value and title depending on comment
     * @param comment comment to be setted
     */
    public void setComment(AudioComment comment){
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

    /**
     * enable/disable player buttons
     * @param enabled true/false
     */
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

    /**
     *
     * @return JPanel with UI
     */
    public JPanel getContent(){
        return playerWindowContent;
    }

}
