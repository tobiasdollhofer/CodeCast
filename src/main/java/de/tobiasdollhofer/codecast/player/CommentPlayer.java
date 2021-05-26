package de.tobiasdollhofer.codecast.player;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CommentPlayer {

    private String path;

    private Media media;
    private MediaPlayer mediaPlayer;

    public void run(){
        System.out.println("Player started");
    }

    public void pause(){
        System.out.println("Player stopped");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

    }
}
