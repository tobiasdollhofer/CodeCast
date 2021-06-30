package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.util.exception.NoFileUrlException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
     * @return if comment is downloaded
     */
    public static boolean checkCommentDownloaded(Project project, AudioComment comment){
        if(getFilePathForComment(project, comment) != null)
            return new File(getFilePathForComment(project, comment)).exists();
        return false;
    }

    /**
     *
     * @param project current project
     * @param comment comment to check
     * @return if comment file is already stored and up to date
     * @throws NoFileUrlException
     */
    public static boolean checkCommentDownloadedUpToDate(Project project, AudioComment comment) throws NoFileUrlException{
        if(getFilePathForComment(project, comment) != null){
            File temp = new File(getFilePathForComment(project, comment));
            if(temp.exists()){
                try{
                    // check if expires exist
                    URL url = new URL(comment.getUrl());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    String expires = con.getHeaderField("expires");
                    if(expires != null){
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                        Date expiresDate = format.parse(expires);
                        Date lastChangeDate = new Date(temp.lastModified());
                        // return false if file is expired
                        if(expiresDate.before(lastChangeDate)){
                            return false;
                        }
                    }
                    con.disconnect();
                }catch(IOException | ParseException e){
                    return false;
                }
                return true;
            }
            return false;
        }
        throw new NoFileUrlException(comment);
    }
}
