package de.tobiasdollhofer.codecast.player.util.event.ui;

import de.tobiasdollhofer.codecast.player.util.event.Event;

public class UIEvent extends Event {

    private UIEventType type;

    public UIEvent(UIEventType type, String data) {
        super(data);
        this.type = type;
    }

    public UIEventType getType() {
        return type;
    }

    public void setType(UIEventType type) {
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
