package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;

@Service
public class PlaylistServiceImpl implements PlaylistService{

    private final Project project;
    private Playlist playlist;

    public PlaylistServiceImpl(Project project) {
        this.project = project;
        loadPlaylist();
        System.out.println(playlist.toString());
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void loadPlaylist() {
        playlist = PlaylistLoader.loadFromMetaFile(FilePathUtil.getCodeCastMetaPath(project));
    }

    @Override
    public void emptyPlaylist() {
        playlist = null;
    }
}
