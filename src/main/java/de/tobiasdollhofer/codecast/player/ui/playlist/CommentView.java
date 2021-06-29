package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.util.DurationFormatter;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CommentView extends JPanel {

    private AudioComment comment;
    private boolean active;
    private JBLabel title;
    private JBLabel length;

    private JBLabel playPause;

    public CommentView(AudioComment comment) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(-1, 40));
        this.comment = comment;
        this.active = false;
        this.playPause = new JBLabel();
        this.playPause.setIcon(PluginIcons.play);
        this.playPause.setPreferredSize(new Dimension(30, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 4,10,0));
        //this.setBorder(BorderFactory.createTitledBorder("comment"));
        title = new JBLabel(comment.getTitleWithoutNumbers());
        length = new JBLabel(DurationFormatter.formatDuration(comment.getDuration()));
        if(comment.getType() == AudioCommentType.INTRO){
            title.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            length.setFont(new Font("SegoeUI", Font.ITALIC, 16));
        }else{
            //TODO: Central Font size
            title.setFont(new Font("SegoeUI", Font.PLAIN, 16));
            length.setFont(new Font("SegoeUI", Font.PLAIN, 16));
        }
        title.setVisible(true);
        //title.setAlignmentX(Component.LEFT_ALIGNMENT);
        //title.setBorder(BorderFactory.createTitledBorder("title"));


        //length.setBorder(BorderFactory.createTitledBorder("length"));
        add(playPause);
        add(title);
        add(Box.createHorizontalGlue());
        add(length);
    }

    public AudioComment getComment() {
        return comment;
    }

    public void setComment(AudioComment comment) {
        this.comment = comment;
    }

    public JBLabel getPlayPauseIcon(){
        return playPause;
    }

    public void setActive(boolean active){
        this.active = active;
        if(active){
            playPause.setIcon(PluginIcons.play);
            title.setFont(new Font("SegoeUI", Font.BOLD, 16));
            length.setFont(new Font("SegoeUI", Font.BOLD, 16));
        }else{
            System.out.println(comment);
            if(comment.getType() == AudioCommentType.INTRO){
                System.out.println("intro");
                title.setFont(new Font("SegoeUI", Font.ITALIC, 16));
                length.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            }else{
                System.out.println("no intro");
                title.setFont(new Font("SegoeUI", Font.PLAIN, 16));
                length.setFont(new Font("SegoeUI", Font.PLAIN, 16));
            }
            playPause.setIcon(null);
        }
    }

    public void play(){
        playPause.setIcon(PluginIcons.pause);
    }

    public void pause(){
        playPause.setIcon(PluginIcons.play);
    }

}
