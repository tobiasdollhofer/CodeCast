package de.tobiasdollhofer.codecast.player.service.playlist;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.playlist.PlaylistLoader;

/**
 * Service to provide Playlist loaded by the PlaylistLoader
 */
@Service
public class PlaylistServiceImpl implements PlaylistService{

    private final Project project;
    private Playlist playlist;

    public PlaylistServiceImpl(Project project) {
        this.project = project;
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
}
