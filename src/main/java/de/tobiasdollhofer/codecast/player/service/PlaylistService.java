package de.tobiasdollhofer.codecast.player.service;

import de.tobiasdollhofer.codecast.player.data.Playlist;

/**
 * Interface for PlaylistService
 */
public interface PlaylistService {

    public Playlist getPlaylist();

    public void loadPlaylist();

    public void emptyPlaylist();
}
