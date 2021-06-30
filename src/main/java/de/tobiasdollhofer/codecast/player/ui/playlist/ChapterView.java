package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Chapter;

import javax.swing.*;
import java.awt.*;

public class ChapterView extends JPanel {

    public ChapterView(Chapter chapter, PlaylistView playlistView) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBorder(BorderFactory.createTitledBorder("chapter"));
        JBLabel label = new JBLabel(chapter.getTitleWithoutNumber());
        label.setFont(new Font("SegoeUI", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        add(label);

        for(int i = 0; i < chapter.getComments().size(); i++) {
            AudioComment comment = chapter.getComment(i);

            CommentView commentView = new CommentView(comment, playlistView);

            add(commentView);


            if (i < chapter.getComments().size() - 1) {
                JSeparator separator = new JSeparator();
                separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
                add(separator);
            }
        }
    }

    public void resetCommentViews(AudioComment current) {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof CommentView) {
                CommentView view = (CommentView) component;
                if (view.getComment().equals(current)) {
                    view.setActive(true);
                } else {
                    view.setActive(false);
                }
            }
        }

    }

    public void playCurrent(AudioComment current) {
        CommentView commentView = findViewForComment(current);
        if(commentView != null)
            commentView.play();
    }

    public void pauseCurrent(AudioComment current) {
        CommentView commentView = findViewForComment(current);
        if(commentView != null)
            commentView.pause();
    }

    private CommentView findViewForComment(AudioComment comment){
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof CommentView) {
                CommentView view = (CommentView) component;
                if (view.getComment().equals(comment)) {
                    return view;
                }
            }
        }
        return null;
    }
}
