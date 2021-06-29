package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
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
        this.comment = comment;
        this.active = false;
        this.playPause = new JBLabel();
        this.playPause.setIcon(PluginIcons.play);
        setBorder(BorderFactory.createEmptyBorder(0, 4,0,0));
        //this.setBorder(BorderFactory.createTitledBorder("comment"));
        title = new JBLabel(comment.getTitleWithoutNumbers());
        title.setFont(new Font("SegoeUI", Font.PLAIN, 14));
        title.setVisible(true);
        //title.setAlignmentX(Component.LEFT_ALIGNMENT);
        //title.setBorder(BorderFactory.createTitledBorder("title"));
        length = new JBLabel(DurationFormatter.formatDuration(comment.getDuration()));
        length.setFont(new Font("SegoeUI", Font.PLAIN, 14));
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
            title.setFont(new Font("SegoeUI", Font.BOLD, 14));
            length.setFont(new Font("SegoeUI", Font.BOLD, 14));
        }else{
            playPause.setIcon(null);
            title.setFont(new Font("SegoeUI", Font.PLAIN, 14));
            length.setFont(new Font("SegoeUI", Font.PLAIN, 14));
        }
    }

    public void play(){
        playPause.setIcon(PluginIcons.pause);
    }

    public void pause(){
        playPause.setIcon(PluginIcons.play);
    }

}
