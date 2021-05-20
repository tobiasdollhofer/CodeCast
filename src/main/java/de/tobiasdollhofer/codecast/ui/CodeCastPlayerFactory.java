package de.tobiasdollhofer.codecast.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;
import org.jetbrains.annotations.NotNull;



public class CodeCastPlayerFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CodeCastPlayer codeCastPlayer = new CodeCastPlayer();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(codeCastPlayer.getContent(), "CodeCast", false);
        toolWindow.getContentManager().addContent(content);
        String path = FilePathUtil.getCodeCastMetaPath(project);
        PlaylistLoader.loadFromMetaFile(path);
    }
}
