package de.tobiasdollhofer.codecast.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CodeCastPlayerFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CodeCastPlayer codeCastPlayer = new CodeCastPlayer();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(codeCastPlayer.getContent(), "CodeCast", false);
        toolWindow.getContentManager().addContent(content);
        /*for(Module m : ModuleManager.getInstance(project).getModules()){
            System.out.println(m.getModuleFilePath());
        }*/
        //PlaylistLoader.loadFromMetaFile("../.codecast/meta.codecast");
        String projectName = project.getName();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentRoots();

        String path = vFiles[0].getPath() + "/.codecast/meta.codecast";
        System.out.println(path);
        PlaylistLoader.loadFromMetaFile(path);
        /*String sourceRootsList = Arrays.stream(vFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"));
        Messages.showInfoMessage("Source roots for the " + projectName + " plugin:\n" + sourceRootsList, "Project Properties");
*/
    }
}
