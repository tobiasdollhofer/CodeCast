package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

/**
 * partly from: https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/max_opened_projects/src/main/java/org/intellij/sdk/maxOpenProjects/ProjectOpenCloseListener.java
 */
public class ProjectOpenCloseListener implements ProjectManagerListener {

    /**
     * initializes services on project startup after index is built up
     * @param project current project
     */
    @Override
    public void projectOpened(@NotNull Project project) {
        ProjectManagerListener.super.projectOpened(project);
        //return if application is in unit-test mode
        if(ApplicationManager.getApplication().isUnitTestMode()){
            return;
        }

        // init services when index is built up
        DumbService.getInstance(project).runWhenSmart(new Runnable() {
            @Override
            public void run() {
                PlaylistService playlistService = project.getService(PlaylistService.class);
                playlistService.loadPlaylist();
                PlayerManagerService playerManagerService = project.getService(PlayerManagerService.class);
            }
        });

    }

    /**
     * clears playlist on project close
     * @param project current project
     */
    @Override
    public void projectClosed(@NotNull Project project) {
        ProjectManagerListener.super.projectClosed(project);

        //return if application is in unit-test mode
        if(ApplicationManager.getApplication().isUnitTestMode()){
            return;
        }

        PlaylistService playlistService = project.getService(PlaylistService.class);
        playlistService.emptyPlaylist();
    }
}
