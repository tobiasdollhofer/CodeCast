package de.tobiasdollhofer.codecast.player.util.event;

/**
 * parent event class used for application specific events
 */
public abstract class Event {

    protected String data;

    public Event(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
