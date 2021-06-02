package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;

/**
 * Methods provide some basic file paths as Strings
 */
public class FilePathUtil {

    public static String getAbsoluteRootFilePath(Project project){
        return ProjectRootManager.getInstance(project).getContentRoots()[0].getPath();
    }

    public static String getCodeCastRootPath(Project project){
        return getAbsoluteRootFilePath(project) + "/.codecast/";
    }

    public static String getCodeCastMetaPath(Project project){
        return getCodeCastRootPath(project) + "meta.codecast";
    }

    public static String getCodeCastAudioDirectory(Project project){
        return getCodeCastRootPath(project) + "audio/";
    }
}
