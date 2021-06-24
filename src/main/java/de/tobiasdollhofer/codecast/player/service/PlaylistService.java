package de.tobiasdollhofer.codecast.player.service;

import de.tobiasdollhofer.codecast.player.data.Playlist;

/**
 * Interface for PlaylistService
 */
public interface PlaylistService {

    Playlist getPlaylist();

    void loadPlaylist();

    void emptyPlaylist();
}
