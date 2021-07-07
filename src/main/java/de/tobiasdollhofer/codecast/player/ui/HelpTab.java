package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.util.ClassLoaderUtil;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HelpTab {

    private JTextPane codeCastTextPane;
    private JPanel helpTabContent;
    private JTextPane introduction;
    private JTextPane reloadDescription;
    private JPanel reloadImg;
    private JPanel autoplayImg;
    private JTextPane autoplayDescription;
    private JPanel jumpToCodeImg;
    private JTextPane jumpToCodeDescription;
    private JPanel showCodeImg;
    private JTextPane showCodeDescription;
    private JPanel gutterImg;
    private JTextPane gutterDescription;


    public HelpTab() {
    }

    private void initializeImages() {
        reloadImg = loadImageInPanel("img/reload.png");
        autoplayImg = loadImageInPanel("img/autoplay.png");
        jumpToCodeImg = loadImageInPanel("/img/jump_to_code.png");
        showCodeImg = loadImageInPanel("/img/show_code.png");
        gutterImg = loadImageInPanel("/img/gutter.png");
    }


    private JPanel loadImageInPanel(String path){
        URL url = this.getClass().getClassLoader().getResource(path);
        if(url != null){
            Image image = ImageLoader.loadFromUrl(url);
            if(image != null){
                return new ImagePanel(image);
            }
        }
        return new JPanel();
    }

    public JPanel getHelpTabContent() {
        return helpTabContent;
    }

    private void createUIComponents() {
        initializeImages();
    }
}

/**
 * Class for button images
 */
class ImagePanel extends JPanel{
    private Image image;

    public ImagePanel(Image image) {
        super();
        this.image = image;
        Dimension size = new Dimension(64,64);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setAlignmentY(Component.CENTER_ALIGNMENT);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
