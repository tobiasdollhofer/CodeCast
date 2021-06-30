package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.android.tools.r8.graph.J;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.UI;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Chapter;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlaylistView extends JPanel {

    private Playlist playlist;
    private Project project;
    private AudioComment current;

    public PlaylistView(Playlist playlist, Project project) {
        this.playlist = playlist;
        this.project = project;
        this.current = playlist.getFirstComment();
        buildView();
    }

    private void buildView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //create margin around view
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // build chapter views
        for(Chapter chapter : playlist.getChapters()){

            ChapterView chapterView = new ChapterView(chapter, this);
            chapterView.add(Box.createVerticalStrut(20));
            add(chapterView);
        }
    }

    private void resetCommentViews() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof ChapterView) {
                ChapterView view = (ChapterView) component;
                view.resetCommentViews(current);
            }
        }
    }

    public void clicked(AudioComment comment){
        if(this.current.equals(comment)){
            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED_SAME, ""));
        }else{
            this.current = comment;
            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED, ""));
        }
    }

    public void playPauseClicked(){
        project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.PLAY_PAUSE_CLICKED, ""));
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        buildView();
    }

    public AudioComment getCurrent() {
        return current;
    }

    public void setCurrent(AudioComment current) {
        this.current = current;
        resetCommentViews();
    }

    public void playCurrent(){
        Component[] components = getComponents();
        for(Component component : components){
            if(component instanceof ChapterView){
                ChapterView view = (ChapterView) component;
                view.playCurrent(current);
            }
        }
    }

    public void pauseCurrent(){
        Component[] components = getComponents();
        for(Component component : components){
            if(component instanceof ChapterView){
                ChapterView view = (ChapterView) component;
                view.pauseCurrent(current);
            }
        }
    }
}
