package de.tobiasdollhofer.codecast.player;


import com.intellij.openapi.project.Project;
import com.sun.javafx.application.PlatformImpl;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.util.DurationFormatter;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEventType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

/**
 * media player for playback
 * notifies all events to its listener
 */
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

    /**
     * starts playback if player is ready
     */
    public void play(){
        if(this.mediaPlayer != null && ready){
            this.playing = true;


            // starts new thread for progress updates
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

    /**
     * pauses playback
     */
    public void pause(){
        if(this.mediaPlayer != null && ready){
            this.mediaPlayer.pause();
            this.playing = false;
            notifyAll(new PlayerEvent(PlayerEventType.STOPPED, ""));
        }
    }

    /**
     * sets player volume
     * @param volume double value between 0 and 1 for volume
     */
    public void setVolume(double volume){
        this.volume = volume;
        if(this.mediaPlayer != null && ready){
            mediaPlayer.setVolume(volume);
        }
    }

    public String getPath() {
        return path;
    }

    /**
     * sets comment path to mediaplayer
     * @param path path to set
     * @param playAlong boolean value if player was paused or played before
     */
    public void setPath(String path, boolean playAlong) {
        this.ready = false;
        this.path = path;
        try{
            //TODO: create media objects for all comments directly?
            this.media = new Media(path);
            this.mediaPlayer = new MediaPlayer(this.media);

            // mark player es ready and play if player was played before
            this.mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    ready = true;
                    CommentPlayer.this.notifyAll(new PlayerEvent(PlayerEventType.INITIALIZED, ""));
                    setVolume(volume);
                    if(playAlong){
                        play();
                    }
                }
            });

            /**
             * notify listener that comment has finished
             */
            this.mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.pause();
                    playing = false;
                    CommentPlayer.this.notifyAll(new PlayerEvent(PlayerEventType.ENDED, ""));
                }
            });


        }catch (IllegalArgumentException | MediaException e){
            e.printStackTrace();
            notifyAll(new PlayerEvent(PlayerEventType.MEDIA_UNAVAILABLE, ""));
        }
    }

    /**
     * @return length in seconds
     */
    public double getLength(){
        if(ready){
            return this.mediaPlayer.getMedia().getDuration().toSeconds();
        }
        return 0;
    }

    /**
     *
     * @return progress of player in pattern x:xx/x:xx
     */
    public String getFormattedProgress(){
        StringBuilder sb = new StringBuilder();
        if(ready){
            sb.append(DurationFormatter.formatDuration(mediaPlayer.getCurrentTime()));
            sb.append('/');
            sb.append(DurationFormatter.formatDuration(mediaPlayer.getMedia().getDuration()));
        }else{
            sb.append("0:00/0:00");
        }
        return sb.toString();
    }

    /**
     *
     * @return value between 0 and 100 of playback progress
     */
    public int getProgressPercentage(){
        double current = mediaPlayer.getCurrentTime().toSeconds();
        double complete = mediaPlayer.getMedia().getDuration().toSeconds();
        double percentage = (current / complete) * 100;
        if (percentage > 99) {
            return 100;
        }
        if (percentage < 1){
            return 1;
        }
        return (int) percentage;
    }

}
