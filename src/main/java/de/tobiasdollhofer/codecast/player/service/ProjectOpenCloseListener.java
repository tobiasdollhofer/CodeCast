package de.tobiasdollhofer.codecast.player.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

/**
 * partly from: https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/max_opened_projects/src/main/java/org/intellij/sdk/maxOpenProjects/ProjectOpenCloseListener.java
 */
public class ProjectOpenCloseListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        ProjectManagerListener.super.projectOpened(project);
        //return if application is in unit-test mode
        if(ApplicationManager.getApplication().isUnitTestMode()){
            return;
        }

        PlaylistService playlistService = project.getService(PlaylistService.class);
        PlayerManagerService playerManagerService = project.getService(PlayerManagerService.class);
    }


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
