package de.tobiasdollhofer.codecast.player.util;

import javafx.util.Duration;

/**
 * util class to provide a formatted representation of a duration instance
 */
public class DurationFormatter {

    /**
     *
     * @param duration time to represent
     * @return string representation of time in pattern xx:xx
     */
    public static String formatDuration(Duration duration){
        if(duration == null) return "";

        double durationSeconds = duration.toSeconds();
        int hours = (int) (durationSeconds / 3600);
        durationSeconds = durationSeconds - 3600 * hours;
        int minutes = (int) (durationSeconds / 60);
        durationSeconds = durationSeconds - 60 * minutes;
        int seconds = (int) durationSeconds;
        StringBuilder sb = new StringBuilder();

        // add hours if necessary
        if(hours > 0){
            sb.append(hours);
            sb.append(':');
        }

        // add leading 0 if necessary
        if(minutes < 10){
            sb.append(0);
        }
        sb.append(minutes);

        sb.append(':');

        // add leading 0 if necessary
        if(seconds < 10){
            sb.append(0);
        }
        sb.append(seconds);
        return sb.toString();
    }
}
