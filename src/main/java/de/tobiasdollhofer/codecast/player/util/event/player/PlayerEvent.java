package de.tobiasdollhofer.codecast.player.util.event.player;

import de.tobiasdollhofer.codecast.player.util.event.Event;

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
        return "PlayerUIEvent{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}


