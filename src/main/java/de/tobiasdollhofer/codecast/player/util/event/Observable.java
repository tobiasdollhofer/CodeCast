package de.tobiasdollhofer.codecast.player.util.event;

import java.util.ArrayList;
import java.util.List;

/**
 * class to extend containing listeners to be notified
 */
public class Observable {

    protected List<Notifiable> listener;

    public Observable(){
        this.listener = new ArrayList<>();
    }

    /**
     *
     * @param listener listener to add
     */
    public void addListener(Notifiable listener){
        this.listener.add(listener);
    }

    /**
     * all listener will be notified to specific event
     * @param event event to notify
     */
    protected void notifyAll(Event event){
        for(Notifiable n : listener){
            n.notify(event);
        }
    }

}
