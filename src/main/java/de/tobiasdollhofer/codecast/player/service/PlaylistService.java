package de.tobiasdollhofer.codecast.player.service;

import de.tobiasdollhofer.codecast.player.data.Playlist;

public interface PlaylistService {

    public Playlist getPlaylist();

    public void loadPlaylist();

    public void emptyPlaylist();
}
