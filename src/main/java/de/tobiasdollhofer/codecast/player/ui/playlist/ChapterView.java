package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Chapter;

import javax.swing.*;
import java.awt.*;

/**
 * Provides View for a single chapter in playlist view
 */
public class ChapterView extends JPanel {

    /**
     * Method builds view for a single chapter
     * @param chapter chapter for which view is created
     * @param playlistView playlistview to which this view belongs to
     */
    public ChapterView(Chapter chapter, PlaylistView playlistView) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createTitle(chapter);
        createCommentViews(chapter, playlistView);
    }

    /**
     * adds a simple label with chapter title to this view
     * @param chapter for which the title is created
     */
    private void createTitle(Chapter chapter) {
        JBLabel label = new JBLabel(chapter.getTitleWithoutNumber());
        label.setFont(new Font("SegoeUI", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        add(label);
    }

    /**
     * Method creates all comment views and adds it to this view
     * @param chapter chapter for which this view is created
     * @param playlistView playlistview to which this view belongs to
     */
    private void createCommentViews(Chapter chapter, PlaylistView playlistView) {
        for(int i = 0; i < chapter.getComments().size(); i++) {
            AudioComment comment = chapter.getComment(i);
            CommentView commentView = new CommentView(comment, playlistView);
            add(commentView);

            // add separation line after all comments except the last one
            if (i < chapter.getComments().size() - 1) {
                addSeparationLine();
            }
        }
    }

    /**
     * adds a simple line to the view
     */
    private void addSeparationLine() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        add(separator);
    }

    /**
     * Method activates / deactivates commentviews depending on provided comment
     * @param current audiocomment for which the view will be set active
     */
    public void resetCommentViews(AudioComment current) {
        Component[] components = getComponents();
        for (Component component : components) {

            // get all commentview components from this view
            if (component instanceof CommentView) {
                CommentView view = (CommentView) component;

                // activate view if it is the view for the current comment
                if (view.getComment().equals(current)) {
                    view.setActive(true);
                } else {
                    view.setActive(false);
                }
            }
        }

    }

    /**
     *
     * @param comment comment to search for
     * @return depending view of the provided comment
     */
    public CommentView findViewForComment(AudioComment comment){
        Component[] components = getComponents();
        for (Component component : components) {

            // get all commentview components from this view
            if (component instanceof CommentView) {
                CommentView view = (CommentView) component;
                if (view.getComment().equals(comment)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * method sets view for current comment to the state "play"
     * @param current comment for which the depending view will be set
     */
    public void playCurrent(AudioComment current) {
        CommentView commentView = findViewForComment(current);
        if(commentView != null)
            commentView.play();
    }

    /**
     * method sets view for current comment to the state "pause"
     * @param current comment for which the depending view will be set
     */
    public void pauseCurrent(AudioComment current) {
        CommentView commentView = findViewForComment(current);
        if(commentView != null)
            commentView.pause();
    }

}
