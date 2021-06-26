package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;


@Service
public class PlaylistServiceImpl implements PlaylistService{

    private final Project project;
    private Playlist playlist;

    public PlaylistServiceImpl(Project project) {
        this.project = project;
        //loadPlaylist();
    }

    /**
     *
     * @return playlist or null
     */
    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * loads playlist from codecast comments using PlaylistLoader
     */
    @Override
    public void loadPlaylist() {
        playlist = PlaylistLoader.loadPlaylistFromComments(project);
    }


    /**
     * sets playlist as null
     */
    @Override
    public void emptyPlaylist() {
        playlist = null;
    }
}
