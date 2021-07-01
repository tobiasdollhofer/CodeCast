package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.tobiasdollhofer.codecast.player.service.playermanager.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import org.jetbrains.annotations.NotNull;



public class PlayerUIFactory implements ToolWindowFactory {

    /**
     * creates ui on project startup
     * @param project current project
     * @param toolWindow window where content will be added
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PlayerUI ui = project.getService(PlayerManagerService.class).getPlayerUI();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(ui.getContent(), Strings.WINDOW_DISPLAY_NAME, false);
        toolWindow.getContentManager().addContent(content);
    }
}
