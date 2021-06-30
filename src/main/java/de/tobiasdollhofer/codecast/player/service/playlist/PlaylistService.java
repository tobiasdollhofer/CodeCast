package de.tobiasdollhofer.codecast.player.service.playlist;

import de.tobiasdollhofer.codecast.player.data.Playlist;

/**
 * Interface for PlaylistService
 */
public interface PlaylistService {

    Playlist getPlaylist();

    void loadPlaylist();

    void emptyPlaylist();
}
