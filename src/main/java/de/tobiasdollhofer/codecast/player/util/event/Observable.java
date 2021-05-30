package de.tobiasdollhofer.codecast.player.util.event;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    protected List<Notifiable> listener;

    public Observable(){
        this.listener = new ArrayList<>();
    }

    public void addListener(Notifiable listener){
        this.listener.add(listener);
    }

    protected void notifyAll(Event event){
        for(Notifiable n : listener){
            n.notify(event);
        }
    }

}
