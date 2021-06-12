package de.tobiasdollhofer.codecast.player.util.event.downloader;

import de.tobiasdollhofer.codecast.player.util.event.Event;

public class DownloadEvent extends Event {
    private DownloadEventType type;

    public DownloadEvent(DownloadEventType type, String data){
        super(data);
        this.type = type;
    }

    public DownloadEventType getType() {
        return type;
    }

    public void setType(DownloadEventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DownloadEvent{" +
                "data='" + data + '\'' +
                ", type=" + type +
                '}';
    }
}
