package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.DurationFormatter;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEventType;

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

    public CommentView(AudioComment comment, PlaylistView playlistView) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(-1, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        this.comment = comment;
        this.active = false;
        this.playPause = new JBLabel();
        this.playPause.setIcon(PluginIcons.play);
        this.playPause.setPreferredSize(new Dimension(30, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 4,10,0));
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
        add(playPause);
        add(title);
        add(Box.createHorizontalGlue());
        add(length);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playlistView.clicked(comment);
            }
        });

        playPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                playlistView.playPauseClicked();
            }
        });
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
            if(comment.getType() == AudioCommentType.INTRO){
                title.setFont(new Font("SegoeUI", Font.ITALIC, 16));
                length.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            }else{
                title.setFont(new Font("SegoeUI", Font.PLAIN, 16));
                length.setFont(new Font("SegoeUI", Font.PLAIN, 16));
            }
            playPause.setIcon(null);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void play(){
        playPause.setIcon(PluginIcons.pause);
    }

    public void pause(){
        playPause.setIcon(PluginIcons.play);
    }

}
