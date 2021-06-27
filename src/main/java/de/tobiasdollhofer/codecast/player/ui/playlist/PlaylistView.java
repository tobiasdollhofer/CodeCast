package de.tobiasdollhofer.codecast.player.ui.playlist;

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
        setAlignmentX(LEFT_ALIGNMENT);
        for(Chapter chapter : playlist.getChapters()){

            ChapterView chapterView = new ChapterView(chapter);
            //chapterView.setSize(this.getSize());

            for(AudioComment comment : chapter.getComments()){

                CommentView commentView = new CommentView(comment);

                commentView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        current = comment;
                        project.getService(PlayerManagerService.class).notify(new UIEvent(UIEventType.LIST_CLICKED, ""));
                    }
                });

                chapterView.add(commentView);
                commentViews.add(commentView);
            }
            chapterView.add(Box.createVerticalStrut(20));
            add(chapterView);
        }
        updateUI();
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
}
