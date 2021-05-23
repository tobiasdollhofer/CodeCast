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

    private Project project;

    public CodeCastPlayer(Project project){
        this.project = project;
        initList();
        initToolbarListener();
        initPlayerControlListener();
    }

    private void initPlayerControlListener() {

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
