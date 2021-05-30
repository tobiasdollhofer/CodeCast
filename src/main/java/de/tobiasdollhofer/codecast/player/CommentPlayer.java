package de.tobiasdollhofer.codecast.player;


import com.sun.javafx.application.PlatformImpl;
import de.tobiasdollhofer.codecast.player.util.event.Observable;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEventType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class CommentPlayer extends Observable {

    private String path;
    private double volume;

    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean ready = false;

    public CommentPlayer() {
        super();
        // necessary to initialize javafx components
        PlatformImpl.startup(() -> {});
    }

    public void run(){
        System.out.println("Listener: " + listener.size());
        notifyAll(new PlayerEvent(PlayerEventType.STARTED, ""));
        System.out.println("Player started");
        if(this.mediaPlayer != null && ready){
            mediaPlayer.play();
        }
    }

    public void pause(){
        System.out.println("Player stopped");
        if(this.mediaPlayer != null && ready){
            mediaPlayer.pause();
        }
        System.out.println(getFormattedProgress());
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

    public void setPath(String path) {
        this.ready = false;
        this.path = path;
        try{
            this.media = new Media(path);
            this.mediaPlayer = new MediaPlayer(this.media);
            this.mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    ready = true;
                    setVolume(volume);
                    System.out.println(getLength());
                    System.out.println(getFormattedProgress());
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

}
