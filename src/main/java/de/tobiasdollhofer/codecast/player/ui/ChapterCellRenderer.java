package de.tobiasdollhofer.codecast.player.ui;

import de.tobiasdollhofer.codecast.player.data.Chapter;

import javax.swing.*;
import java.awt.*;

public class ChapterCellRenderer extends JLabel implements javax.swing.ListCellRenderer {


    public ChapterCellRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Chapter chapter = (Chapter) value;
        setText(chapter.getTitle());
        return this;
    }
}
