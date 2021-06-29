package de.tobiasdollhofer.codecast.player.ui.playlist;

import com.intellij.ui.components.JBLabel;
import de.tobiasdollhofer.codecast.player.data.Chapter;

import javax.swing.*;
import java.awt.*;

public class ChapterView extends JPanel {

    public ChapterView(Chapter chapter) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBorder(BorderFactory.createTitledBorder("chapter"));
        JBLabel label = new JBLabel(chapter.getTitleWithoutNumber());
        label.setFont(new Font("SegoeUI", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
        setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        add(label);
    }

}
