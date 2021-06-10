package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import de.tobiasdollhofer.codecast.player.data.AudioComment;

import java.io.File;

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

    public static String getFilePathForComment(Project project, AudioComment comment){
        if(!comment.getFileName().equals("")){
            return getCodeCastAudioDirectory(project) + comment.getFileName();
        }
        return null;
    }

    public static String getFilePathForCommentWithPrefix(Project project, AudioComment comment){
        return "file:///" + getFilePathForComment(project, comment);
    }

    public static boolean checkCommentDownloaded(Project project, AudioComment comment){
        if(getFilePathForComment(project, comment) != null){
            File temp = new File(getFilePathForComment(project, comment));
            return temp.exists();
        }
        return false;
    }
}
