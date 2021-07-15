package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.ui.playlist.PlaylistView;
import de.tobiasdollhofer.codecast.player.util.constants.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;
import de.tobiasdollhofer.codecast.player.util.logging.Context;
import de.tobiasdollhofer.codecast.player.util.logging.CsvLogger;
import de.tobiasdollhofer.codecast.player.util.logging.EventType;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

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
    private JToggleButton jumpToCode;
    private JButton showCodeButton;
    private JTextPane explanation;

    private final boolean playing = false;
    private Playlist playlist;
    private AudioComment comment;
    public String playerTest;
    private PlaylistView playlistView;

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
        showCodeButton.setIcon(PluginIcons.jumpToCode);
        reloadButton.setIcon(PluginIcons.refresh);
    }

    private void initPlayerControlListener() {
        playFirst.addActionListener(e -> playFirstClicked());
        playPrevious.addActionListener(e -> playPreviousClicked());
        playPause.addActionListener(e -> playPauseClicked());
        playNext.addActionListener(e -> playNextClicked());
        playLast.addActionListener(e -> playLastClicked());
        volumeSlider.addChangeListener(e -> volumeSliderChange());
        showCodeButton.addActionListener(e -> showCodeButtonClicked());
        initPlayerProgressBarListener();
    }

    private void initPlayerProgressBarListener(){
        playerProgressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                double progress = e.getX();
                progress = progress / playerProgressBar.getWidth();
                PlayerUI.this.notifyAll(new UIEvent(PROGRESSBAR_CLICKED, String.valueOf(progress)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                double progress = e.getX();
                progress = progress / playerProgressBar.getWidth();
                PlayerUI.this.notifyAll(new UIEvent(PROGRESSBAR_CLICKED, String.valueOf(progress)));
            }
        });
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
    private void showCodeButtonClicked() {
        notifyAll(new UIEvent(SHOW_CODE_CLICKED, ""));
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
        notifyAll(new UIEvent(PLAY_PAUSE_CLICKED, String.valueOf(!playing)));
    }

    /**
     * sets play icon to button
     */
    public void pausePlayer(){
        playPause.setIcon(PluginIcons.play);
        playlistView.pauseCurrent();
    }

    /**
     * sets pause icon to button
     */
    public void playPlayer(){
        playPause.setIcon(PluginIcons.pause);
        playlistView.playCurrent();
    }

    /**
     * display current time progress
     * @param time string value of time ( x:xx/x:xx)
     */
    public void setProgressTime(String time){
        if(!time.equals(progressTime.getText())){
            StyledDocument doc = progressTime.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
        }
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
        autoplayButton.setIcon(PluginIcons.autoPlayOff);
        autoplayButton.addActionListener(e -> autoplayButtonClicked());
        reloadButton.addActionListener(e -> reloadPlayer());
        jumpToCode.addActionListener(e -> jumpToCodeButtonClicked());
        jumpToCode.setIcon(PluginIcons.showCodeOff);
    }

    /**
     * notifies all observer about click event and sets text to ON/OFF
     */
    private void autoplayButtonClicked() {
        if(autoplayButton.isSelected()){
            autoplayButton.setIcon(PluginIcons.autoPlayOn);
        }else{
            autoplayButton.setIcon(PluginIcons.autoPlayOff);
        }
        notifyAll(new UIEvent(AUTOPLAY_CLICKED, String.valueOf(autoplayButton.isSelected())));
    }

    /**
     * notifies all observer about click event and sets text to ON/OFF
     */
    private void jumpToCodeButtonClicked(){
        if(jumpToCode.isSelected()){
            jumpToCode.setIcon(PluginIcons.showCodeOn);
        }else{
            jumpToCode.setIcon(PluginIcons.showCodeOff);
        }
        notifyAll(new UIEvent(JUMP_TO_CODE_CLICKED, String.valueOf(jumpToCode.isSelected())));
    }
    /**
     *
     * @return if autoplaybutton is selected
     */
    public boolean getAutoplayStatus(){
        return autoplayButton.isSelected();
    }

    /**
     *
     * @return if jump to code is activated
     */
    public boolean getJumpToCodeStatus(){
        return jumpToCode.isSelected();
    }
    /**
     * notifies all observer about click event
     */
    private void reloadPlayer() {
        notifyAll(new UIEvent(UIEventType.RESET_PLAYER, "Reloaded player"));
    }

    /**
     * @param playlist playlist to set to playlistList
     */
    public void setPlaylist(Playlist playlist) {
        playlistView = new PlaylistView(playlist, project);
        playlistPane.setViewportView(playlistView);
        playlistPane.updateUI();
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
            playlistView.setCurrent(comment);
            enablePlayer(true);
        }else {
            enablePlayer(false);
        }
    }

    /**
     *
     * @return selected value from list
     */
    public AudioComment getSelectedListComment(){
        if(playlistPane.getViewport().getComponent(0) != null){
           PlaylistView playlistView = (PlaylistView) playlistPane.getViewport().getComponent(0);
           return playlistView.getCurrent();
        }
        return null;
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
        this.showCodeButton.setEnabled(enabled);
        this.playlistPane.setEnabled(enabled);
        // add some placeholder if player is disabled
        if(!enabled){
            this.currentTitleLabel.setText(Strings.NO_COMMENT_AVAILABLE);
            this.playerProgressBar.setValue(0);
            this.progressTime.setText(Strings.PLAYBACK_ZERO);
        }
    }

    /**
     *
     * @return JPanel with UI
     */
    public JPanel getContent(){
        return playerWindowContent;
    }

    private void createUIComponents() {
        playlistPane = new JBScrollPane();
        playlistPane.setLayout(new ScrollPaneLayout());
        playlistPane.setPreferredSize(new Dimension(-1, -1));
        playlistPane.setVisible(true);
    }
}
