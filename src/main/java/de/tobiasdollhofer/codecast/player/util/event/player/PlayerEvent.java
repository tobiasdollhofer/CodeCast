package de.tobiasdollhofer.codecast.player.util.event.player;

import de.tobiasdollhofer.codecast.player.util.event.Event;

/**
 * Event entity for events in context to audio player
 */
public class PlayerEvent extends Event {

    private PlayerEventType type;

    public PlayerEvent(PlayerEventType type, String data) {
        super(data);
        this.type = type;
    }

    public PlayerEventType getType() {
        return type;
    }

    public void setType(PlayerEventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PlayerEvent{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}


