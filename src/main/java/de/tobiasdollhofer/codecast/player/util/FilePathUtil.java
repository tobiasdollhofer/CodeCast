package de.tobiasdollhofer.codecast.player.util;

import com.android.tools.r8.graph.S;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import de.tobiasdollhofer.codecast.player.data.AudioComment;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Methods provide some basic file paths as Strings
 */
public class FilePathUtil {

    /**
     *
     * @param project current project
     * @return absolute root path of project
     */
    public static String getProjectRootPath(Project project){
        return ProjectRootManager.getInstance(project).getContentRoots()[0].getPath();
    }

    /**
     *
     * @return absolute path to codecast root directory
     */
    public static String getCodeCastRootDirectory(){
        return System.getProperty("user.home") + "/" + "codecast" + "/";
    }

    /**
     *
     * @param project current project
     * @return absolute folder path of audio files
     */
    public static String getCodeCastProjectRootDirectory(Project project){
        return getCodeCastRootDirectory() + project.getName() + "/";
    }

    /**
     *
     * @param project current project
     * @param comment comment for which filepath is needed
     * @return absolute filepath or null of comment file
     */
    public static String getFilePathForComment(Project project, AudioComment comment){
        if(!comment.getFileName().equals("")){
            return getCodeCastProjectRootDirectory(project) + comment.getFileName();
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
        File file = new File(getFilePathForComment(project, comment));
        return file.toURI().toASCIIString();
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
