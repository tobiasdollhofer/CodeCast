package de.tobiasdollhofer.codecast.player.util;

import de.tobiasdollhofer.codecast.player.data.AudioComment;

public class NoFileUrlException extends Exception{

    public NoFileUrlException(AudioComment comment) {
        super("URL of comment \n" + comment.getTitle() + "\nis no file URL!");
    }
}
