package de.tobiasdollhofer.codecast.player.util;

import de.tobiasdollhofer.codecast.player.data.AudioComment;

/**
 * Custom exception when file url doesn't end with a file with suffix
 */
public class NoFileUrlException extends Exception{

    public NoFileUrlException(AudioComment comment) {
        super("URL of comment \n" + comment.getTitle() + "\nis no file URL!");
    }
}
