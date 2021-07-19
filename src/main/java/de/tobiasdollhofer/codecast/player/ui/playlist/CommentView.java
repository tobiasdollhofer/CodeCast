package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.util.DurationFormatter;
import de.tobiasdollhofer.codecast.player.util.constants.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import de.tobiasdollhofer.codecast.player.util.notification.BalloonNotifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * View for a single comment in a chapterview in a playlistview
 */
public class CommentView extends JPanel {

    private AudioComment comment;
    private boolean active;
    private JBLabel title;
    private JBLabel length;
    private JBLabel playPause;

    /**
     *
     * @param comment comment for which this view will be created
     * @param playlistView playlistview which will be notified for click events
     */
    public CommentView(AudioComment comment, PlaylistView playlistView) {
        this.comment = comment;
        this.active = false;
        createView(playlistView);
    }

    /**
     * creates all parts of the view
     * @param playlistView playlistview which will be notified for click events
     */
    private void createView(PlaylistView playlistView) {
        setViewLayout();
        createPlayPause();
        createLabels();
        addListener(playlistView);
    }

    /**
     * basic layout properties for this view
     */
    private void setViewLayout() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(-1, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(10, 4,10,0));
    }



    /**
     * creates title and length labels
     */
    private void createLabels() {
        title = new JBLabel(comment.getTitleWithoutNumbers());
        length = new JBLabel(DurationFormatter.formatDuration(comment.getDuration()));
        if(comment.isDownloaded()){
           setViewInactive();
        }else{
            setViewDisabled();
        }
        add(title);
        add(Box.createHorizontalGlue());
        add(length);
    }

    /**
     * creates play pause button
     */
    private void createPlayPause() {
        this.playPause = new JBLabel();
        this.playPause.setIcon(PluginIcons.play);
        this.playPause.setPreferredSize(new Dimension(30, 40));
        add(playPause);
    }

    /**
     * click listener for whole view and playpause button
     * @param playlistView playlistview which will be notified
     */
    private void addListener(PlaylistView playlistView) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(comment.isDownloaded()){
                    playlistView.clicked(comment);
                }else{
                    BalloonNotifier.notifyWarning(playlistView.getProject(), Strings.FILE_NOT_AVAILABLE +  comment.getUrl());
                }
            }
        });

        playPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(comment.isDownloaded()){
                    playlistView.playPauseClicked();
                }else{
                    BalloonNotifier.notifyWarning(playlistView.getProject(), Strings.FILE_NOT_AVAILABLE + comment.getUrl());
                }
            }
        });

    }


    public AudioComment getComment() {
        return comment;
    }

    public void setComment(AudioComment comment) {
        this.comment = comment;
    }

    /**
     * method sets icon and font types depending on if it is active or not
     * @param active if this view is active or not
     */
    public void setActive(boolean active){
        this.active = active;
        if(active){
           setViewActive();
        }else{
            setViewInactive();
        }
    }

    public boolean isActive() {
        return active;
    }

    /**
     * lets play-icon appear and sets active font size (bold)
     */
    private void setViewActive(){
        playPause.setIcon(PluginIcons.play);
        title.setFont(new Font("SegoeUI", Font.BOLD, 16));
        length.setFont(new Font("SegoeUI", Font.BOLD, 16));
    }

    /**
     * grey out labels if disabled
     */
    private void setViewDisabled(){
        playPause.setIcon(null);
        title.setForeground(new Color(124,124,124));
        length.setForeground(new Color(124,124,124));
    }

    /**
     * lets play-icon disappear and sets inactive font-size (normal)
     */
    private void setViewInactive(){
        if(comment.getType() == AudioCommentType.INTRO){
            title.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            length.setFont(new Font("SegoeUI", Font.ITALIC, 16));
        }else{
            title.setFont(new Font("SegoeUI", Font.PLAIN, 16));
            length.setFont(new Font("SegoeUI", Font.PLAIN, 16));
        }
        playPause.setIcon(null);
    }

    /**
     * sets icon to pause-icon
     */
    public void play(){
        playPause.setIcon(PluginIcons.pause);
    }

    /**
     * sets icon to play-icon
     */
    public void pause(){
        playPause.setIcon(PluginIcons.play);
    }

}
