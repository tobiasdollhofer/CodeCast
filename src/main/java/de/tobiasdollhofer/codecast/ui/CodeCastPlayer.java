package de.tobiasdollhofer.codecast.ui;

import com.android.tools.r8.graph.J;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ClickListener;
import com.intellij.ui.components.JBList;
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

    private Project project;

    public CodeCastPlayer(Project project){
        this.project = project;
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
    }

    private void playNextClicked() {
        System.out.println("Play Next clicked!");
    }

    private void playPauseClicked() {
        System.out.println("Play Pause clicked!");
        if(playing){
            playPause.setIcon(PluginIcons.play);
        }else{
            playPause.setIcon(PluginIcons.pause);
        }
        playing = !playing;

    }

    private void playPreviousClicked() {
        System.out.println("Play Previous clicked!");
    }

    private void playFirstClicked() {
        System.out.println("Play First clicked!");
    }

    private void initToolbarListener() {
        reloadButton.addActionListener(e -> reloadPlayer());
    }

    private void reloadPlayer() {
        //TODO: implement
        System.out.println("Reload Player");
    }

    private void initList(){
        Playlist playlist = project.getService(PlaylistService.class).getPlaylist();

    }

    public JPanel getContent(){
        return playerWindowContent;
    }


}
