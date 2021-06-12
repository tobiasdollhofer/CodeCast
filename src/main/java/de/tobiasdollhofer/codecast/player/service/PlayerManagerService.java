package de.tobiasdollhofer.codecast.player.service;

import de.tobiasdollhofer.codecast.player.ui.PlayerUI;
import de.tobiasdollhofer.codecast.player.util.event.Event;

public interface PlayerManagerService {

    PlayerUI getPlayerUI();

    void notify(Event e);
}
