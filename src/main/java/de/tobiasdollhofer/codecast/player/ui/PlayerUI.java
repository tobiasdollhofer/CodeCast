package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

    private final boolean playing = false;
    private Playlist playlist;
    private AudioComment comment;
    public String playerTest;

    private final Project project;

    public PlayerUI(Project project){
        super();
        this.project = project;
        initToolbarListener();
        initPlayerControls();
        initListControls();
    }

    private void initListControls() {
        playlistList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayerUI.this.notifyAll(new UIEvent(LIST_CLICKED, ""));
            }
        });
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
        showCodeButton.setIcon(PluginIcons.showCodeOff);
    }

    private void initPlayerControlListener() {
        playFirst.addActionListener(e -> playFirstClicked());
        playPrevious.addActionListener(e -> playPreviousClicked());
        playPause.addActionListener(e -> playPauseClicked());
        playNext.addActionListener(e -> playNextClicked());
        playLast.addActionListener(e -> playLastClicked());
        volumeSlider.addChangeListener(e -> volumeSliderChange());
        showCodeButton.addActionListener(e -> showCodeButtonClicked());
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
        System.out.println("SET PLAYLIST");
        ArrayList<AudioComment> comments = playlist.getAllComments();
        DefaultListModel<AudioComment> listModel = new DefaultListModel<>();
        listModel.addAll(comments);
        playlistList.setModel(listModel);
        playlistList.setCellRenderer(new CommentRenderer());
        playlistList.setSelectedIndex(0);
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
            enablePlayer(false);
        }
    }

    /**
     *
     * @return selected value from list
     */
    public AudioComment getSelectedListComment(){
        return (AudioComment) playlistList.getSelectedValue();
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
        // add some placeholder if player is disabled
        if(!enabled){
            this.currentTitleLabel.setText("No comment available.");
            this.playlistList.setModel(new DefaultListModel());
            this.playerProgressBar.setValue(0);
            this.progressTime.setText("0:00/0:00");
        }
    }

    /**
     *
     * @return JPanel with UI
     */
    public JPanel getContent(){
        return playerWindowContent;
    }

}
