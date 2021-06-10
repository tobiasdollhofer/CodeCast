package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.Playlist;

import static de.tobiasdollhofer.codecast.player.util.PlaylistLoader.loadPlaylistFromComments;

@Service
public class PlaylistServiceImpl implements PlaylistService{

    private final Project project;
    private Playlist playlist;

    public PlaylistServiceImpl(Project project) {
        this.project = project;
        loadPlaylist();
    }

    @Override
    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void loadPlaylist() {
        //playlist = PlaylistLoader.loadFromMetaFile(FilePathUtil.getCodeCastMetaPath(project));
        DumbService.getInstance(project).runWhenSmart(new Runnable() {
            @Override
            public void run() {
                loadPlaylistFromComments(project);
            }
        });

    }



    @Override
    public void emptyPlaylist() {
        playlist = null;
    }
}
