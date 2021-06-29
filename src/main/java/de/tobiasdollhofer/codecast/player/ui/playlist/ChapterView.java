package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.Chapter;

import javax.swing.*;
import java.awt.*;

public class ChapterView extends JPanel {

    public ChapterView(Chapter chapter) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBorder(BorderFactory.createTitledBorder("chapter"));
        JBLabel label = new JBLabel(chapter.getTitle());
        label.setFont(new Font("SegoeUI", Font.ITALIC, 18));
        //label.setAlignmentX(0);
        //label.setBorder(BorderFactory.createTitledBorder("chapter title"));
        add(label);
    }

}
