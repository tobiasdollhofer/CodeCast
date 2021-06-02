package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import org.jetbrains.annotations.NotNull;



public class PlayerUIFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //CodeCastPlayer codeCastPlayer = new CodeCastPlayer(project);
        PlayerUI ui = project.getService(PlayerManagerService.class).getPlayerUI();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(ui.getContent(), "CodeCast", false);
        toolWindow.getContentManager().addContent(content);
        /*String path = FilePathUtil.getCodeCastMetaPath(project);
        PlaylistLoader.loadFromMetaFile(path);*/
    }
}