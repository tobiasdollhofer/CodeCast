package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import de.tobiasdollhofer.codecast.player.data.AudioComment;

import java.io.File;

/**
 * Methods provide some basic file paths as Strings
 */
public class FilePathUtil {

    /**
     *
     * @param project current project
     * @return absolute root path of project
     */
    public static String getAbsoluteRootFilePath(Project project){
        return ProjectRootManager.getInstance(project).getContentRoots()[0].getPath();
    }

    /**
     *
     * @param project current project
     * @return absolute root folder path of codecast files
     */
    public static String getCodeCastRootPath(Project project){
        return getAbsoluteRootFilePath(project) + "/.codecast/";
    }
 //TODO remove
    public static String getCodeCastMetaPath(Project project){
        return getCodeCastRootPath(project) + "meta.codecast";
    }

    /**
     *
     * @param project current project
     * @return absolute folder path of audio files
     */
    public static String getCodeCastAudioDirectory(Project project){
        return getCodeCastRootPath(project) + "audio/";
    }

    /**
     *
     * @param project current project
     * @param comment comment for which filepath is needed
     * @return absolute filepath or null of comment file
     */
    public static String getFilePathForComment(Project project, AudioComment comment){
        if(!comment.getFileName().equals("")){
            return getCodeCastAudioDirectory(project) + comment.getFileName();
        }
        return null;
    }

    /**
     *
     * @param project current project
     * @param comment comment for which filepath is needed
     * @return adds file:/// to filepath for mediaplayer
     */
    public static String getFilePathForCommentWithPrefix(Project project, AudioComment comment){
        return "file:///" + getFilePathForComment(project, comment);
    }

    /**
     *
     * @param project current project
     * @param comment comment to check
     * @return if comment file is already stored
     * @throws NoFileUrlException
     */
    public static boolean checkCommentDownloaded(Project project, AudioComment comment) throws NoFileUrlException {
        if(getFilePathForComment(project, comment) != null){
            File temp = new File(getFilePathForComment(project, comment));
            return temp.exists();
        }
        throw new NoFileUrlException(comment);
    }
}
