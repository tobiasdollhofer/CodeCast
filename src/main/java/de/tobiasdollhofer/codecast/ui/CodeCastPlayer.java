package de.tobiasdollhofer.codecast.ui;

import com.android.tools.r8.graph.J;
import com.android.tools.r8.graph.S;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ClickListener;
import com.intellij.ui.components.JBList;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Chapter;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.PlaylistService;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;

import javax.swing.*;

public class CodeCastPlayer {

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
    private CommentPlayer player;

    private final Project project;

    public CodeCastPlayer(Project project){
        this.project = project;
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
        this.player = new CommentPlayer();
        if(this.playlist != null){
            setComment(this.playlist.getFirstComment());
        }else{
            enablePlayer(false);
            // TODO: ALERT NO XML!!
        }

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
        if(volumeSlider.getValue() == 0){
            volumeIcon.setIcon(PluginIcons.volumeOff);
        }else{
            volumeIcon.setIcon(PluginIcons.volume);
        }
    }

    private void playLastClicked() {
        System.out.println("Play Last clicked!");
        AudioComment comment = playlist.getLastComment();

        if(comment != null)
            setComment(comment);
    }

    private void playNextClicked() {
        System.out.println("Play Next clicked!");
        AudioComment comment = playlist.getNextComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    private void playPauseClicked() {
        System.out.println("Play Pause clicked!");
        if(playing){
            playPause.setIcon(PluginIcons.play);
            player.pause();
        }else{
            playPause.setIcon(PluginIcons.pause);
            player.run();
        }
        playing = !playing;
        System.out.println("CodeCast-Player State: " + this.playing);
    }

    private void playPreviousClicked() {
        System.out.println("Play Previous clicked!");
        //TODO: add some cooldown to restart current comment
        AudioComment comment = playlist.getPreviousComment(this.comment);

        if(comment != null)
            setComment(comment);
    }

    private void playFirstClicked() {
        System.out.println("Play First clicked!");
        AudioComment comment = playlist.getFirstComment();

        if(comment != null)
            setComment(comment);
    }

    private void initToolbarListener() {
        reloadButton.addActionListener(e -> reloadPlayer());
    }

    private void reloadPlayer() {
        System.out.println("Reload Player");
        project.getService(PlaylistService.class).loadPlaylist();
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
        if(this.playlist != null){
            setComment(this.playlist.getFirstComment());
        }
    }

    private void initList(){
    }

    private void setComment(AudioComment comment){
        if(comment != null){
            this.comment = comment;
            currentTitleLabel.setText(comment.getTitle());
            enablePlayer(true);
        }else {
            currentTitleLabel.setText("No comment available.");
            enablePlayer(false);
        }
        playerProgressBar.setValue(0);
        playing = false;
        playPause.setIcon(PluginIcons.play);
        player.pause();

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
