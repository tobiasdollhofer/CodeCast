package de.tobiasdollhofer.codecast.player.util.event;

/**
 * Interface which can be implemented to get notified to specific events. Used in PlayerManagerServiceImpl class
 */
public interface Notifiable {

    void notify(Event e);
}
