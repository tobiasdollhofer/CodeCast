package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.util.ui.JBUI;
import de.tobiasdollhofer.codecast.player.data.AudioComment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CommentRenderer extends JLabel implements ListCellRenderer<AudioComment> {

    @Override
    public Component getListCellRendererComponent(JList<? extends AudioComment> list, AudioComment value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.getChapter() + "  -  " + value.getTitle());
        setBorder(JBUI.Borders.empty(5));
        return this;
    }
}
