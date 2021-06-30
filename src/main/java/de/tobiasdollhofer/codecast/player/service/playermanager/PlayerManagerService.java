package de.tobiasdollhofer.codecast.player.service.playermanager;

import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.ui.PlayerUI;
import de.tobiasdollhofer.codecast.player.util.event.Event;

/**
 * interface for PlayerManager service to manage UI and Audioplayer
 */
public interface PlayerManagerService {

    PlayerUI getPlayerUI();

    void setPlaylistCommentForFoundComment(AudioComment comment);

    void notify(Event e);
}
