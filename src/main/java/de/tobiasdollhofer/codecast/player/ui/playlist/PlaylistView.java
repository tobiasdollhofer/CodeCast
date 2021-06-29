package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.android.tools.r8.graph.J;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
    private ArrayList<CommentView> commentViews;
    private Project project;
    private AudioComment current;

    public PlaylistView(Playlist playlist, Project project) {
        this.playlist = playlist;
        this.project = project;
        this.current = playlist.getFirstComment();
        this.commentViews = new ArrayList<>();
        buildView();
    }

    private void buildView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //
        //setAlignmentX(LEFT_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        //setBorder(BorderFactory.createTitledBorder("PlaylistView"));
        for(Chapter chapter : playlist.getChapters()){

            ChapterView chapterView = new ChapterView(chapter);
            //chapterView.setSize(this.getSize());

            for(int i = 0; i < chapter.getComments().size(); i++){
                AudioComment comment = chapter.getComment(i);

                CommentView commentView = new CommentView(comment);
                commentView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(current.equals(comment)){
                            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED_SAME, ""));
                        }else{
                            current = comment;
                            project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED, ""));
                        }
                    }
                });
                commentView.getPlayPauseIcon().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.PLAY_PAUSE_CLICKED, ""));
                    }
                });
                commentView.setAlignmentX(Component.LEFT_ALIGNMENT);
                //commentView.setSize(new Dimension(650, 30));
                chapterView.add(commentView);
                commentViews.add(commentView);

                if(i < chapter.getComments().size() - 1){
                    JSeparator separator = new JSeparator();
                    separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
                    chapterView.add(separator);
                }

            }
            chapterView.add(Box.createVerticalStrut(20));
            add(chapterView);
        }
    }

    private void resetCommentViews() {
        for(CommentView commentView : commentViews){
            if(commentView.getComment().equals(current)){
                commentView.setActive(true);
            }else{
                commentView.setActive(false);
            }
        }
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
        for(CommentView commentView : commentViews){
            if(commentView.getComment().equals(current)){
                commentView.play();
            }
        }
    }

    public void pauseCurrent(){
        for(CommentView commentView : commentViews){
            if(commentView.getComment().equals(current)){
                commentView.pause();
            }
        }
    }
}
