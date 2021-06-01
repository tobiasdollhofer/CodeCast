package de.tobiasdollhofer.codecast.player;


import com.sun.javafx.application.PlatformImpl;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEventType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class CommentPlayer extends Observable {

    private String path;
    private double volume;

    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean ready = false;
    // duplicate to mediaplayer.getStatus() -> used because player needs some time to start and stop asynchronously
    private boolean playing = false;

    public CommentPlayer() {
        super();

        this.volume = 1;
        // necessary to initialize javafx components
        PlatformImpl.startup(() -> {});
    }

    public void play(){
        if(this.mediaPlayer != null && ready){
            this.playing = true;


            Thread updateProgressThread = new Thread(() -> {
                while (playing) {
                    notifyAll(new PlayerEvent(PlayerEventType.PROGRESS_CHANGED, ""));
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


            updateProgressThread.start();
            this.mediaPlayer.play();
            notifyAll(new PlayerEvent(PlayerEventType.STARTED, ""));
        }
    }

    public void pause(){
        if(this.mediaPlayer != null && ready){
            this.mediaPlayer.pause();
            this.playing = false;
            notifyAll(new PlayerEvent(PlayerEventType.STOPPED, ""));
        }
    }

    public void setVolume(double volume){
        this.volume = volume;
        if(this.mediaPlayer != null && ready){
            mediaPlayer.setVolume(volume);
        }
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path, boolean playAlong) {
        this.ready = false;
        this.path = path;
        try{
            this.media = new Media(path);
            this.mediaPlayer = new MediaPlayer(this.media);
            this.mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {

                    CommentPlayer.this.notifyAll(new PlayerEvent(PlayerEventType.INITIALIZED, ""));
                    ready = true;
                    setVolume(volume);
                    if(playAlong){
                        play();
                    }
                }
            });

            this.mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.pause();
                    playing = false;
                    CommentPlayer.this.notifyAll(new PlayerEvent(PlayerEventType.ENDED, ""));
                }
            });


        }catch (IllegalArgumentException e){
            e.printStackTrace();
            //TODO: Message Alert - MP3 not available
        }
    }

    public double getLength(){
        if(ready){
            return this.mediaPlayer.getMedia().getDuration().toSeconds();
        }
        return 0;
    }

    public String getFormattedProgress(){
        StringBuilder sb = new StringBuilder();
        if(ready){
            sb.append(getFormattedTime(mediaPlayer.getCurrentTime()));
            sb.append('/');
            sb.append(getFormattedTime(mediaPlayer.getMedia().getDuration()));
        }else{
            sb.append("0:00/0:00");
        }
        return sb.toString();
    }

    private String getFormattedTime(Duration duration){
        double durationSeconds = duration.toSeconds();
        int hours = (int) (durationSeconds / 3600);
        durationSeconds = durationSeconds - 3600 * hours;
        int minutes = (int) (durationSeconds / 60);
        durationSeconds = durationSeconds - 60 * minutes;
        int seconds = (int) durationSeconds;
        StringBuilder sb = new StringBuilder();
        if(hours > 0){
            sb.append(hours);
            sb.append(':');
        }
        sb.append(minutes);
        sb.append(':');
        if(seconds < 10){
            sb.append(0);
        }
        sb.append(seconds);
        return sb.toString();
    }

    public int getProgressPercentage(){
        double current = mediaPlayer.getCurrentTime().toSeconds();
        double complete = mediaPlayer.getMedia().getDuration().toSeconds();
        double percentage = (current / complete) * 100;
        if (percentage > 99) {
            return 100;
        }
        return (int) percentage;
    }
}
