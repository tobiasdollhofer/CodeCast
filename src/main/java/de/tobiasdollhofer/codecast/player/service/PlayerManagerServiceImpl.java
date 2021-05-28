package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.CommentPlayer;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.event.*;
import de.tobiasdollhofer.codecast.player.util.event.player.PlayerEvent;
import de.tobiasdollhofer.codecast.player.util.event.ui.UIEvent;
import de.tobiasdollhofer.codecast.player.ui.PlayerUI;

@Service
public class PlayerManagerServiceImpl implements PlayerManagerService, Notifiable {

    private Playlist playlist;
    private final Project project;
    private AudioComment comment;
    private CommentPlayer player;
    private PlayerUI ui;

    public PlayerManagerServiceImpl(Project project) {
        this.project = project;
        this.playlist = project.getService(PlaylistService.class).getPlaylist();
        this.player = new CommentPlayer();
        this.ui = new PlayerUI(project);
        if(playlist != null){
            this.comment = playlist.getFirstComment();
            this.player.setPath(this.comment.getPath());
            this.ui.setComment(this.comment);
        }
        addListeners();
    }

    private void addListeners() {
        player.addListener(this);
        ui.addListener(this);
    }


    @Override
    public void notify(Event e) {
        System.out.println("Notified: " + e.toString());
        if(e.getClass().equals(PlayerEvent.class)){
            notifyPlayerEvent((PlayerEvent) e);
        }else if(e.getClass().equals(UIEvent.class)){
            notifyUIEvent((UIEvent) e);
        }
    }

    private void notifyUIEvent(UIEvent e) {
        switch(e.getType()){
            case PLAY_FIRST_CLICKED:

                break;

            case PLAY_PREVIOUS_CLICKED:

                break;

            case PLAY_PAUSE_CLICKED:

                break;

            case PLAY_NEXT_CLICKED:

                break;

            case PLAY_LAST_CLICKED:

                break;

            case VOLUME_CHANGE:

                break;

            case LIST_CLICKED:

                break;

            case RESET_PLAYER:

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }


    }

    private void notifyPlayerEvent(PlayerEvent e) {
        switch (e.getType()){
            case INITIALIZED:

                break;

            case STARTED:

                break;

            case STOPPED:

                break;

            case ENDED:

                break;

            case PROGRESS_CHANGED:

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + e.getType());
        }
    }

    @Override
    public PlayerUI getPlayerUI() {
        return ui;
    }
}
