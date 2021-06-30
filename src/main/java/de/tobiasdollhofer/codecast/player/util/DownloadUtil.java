package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.playermanager.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEvent;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEventType;
import de.tobiasdollhofer.codecast.player.util.exception.NoFileUrlException;
import de.tobiasdollhofer.codecast.player.util.notification.BalloonNotifier;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class DownloadUtil {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads all comment files of playlist which aren't downloaded yet
     * notifies PlayerManagerService if job was finished or canceled
     * @param project current project
     * @param playlist playlist with comments to download
     */
    public static void downloadComments(Project project, Playlist playlist){
        ArrayList<AudioComment> comments = getCommentsToDownload(project, playlist.getAllComments());

        /**
         * download files in backgroundtask
         */
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Downloading CodeCast Audiofiles...") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                File codecastRoot = new File(FilePathUtil.getCodeCastRootDirectory());
                if(!codecastRoot.exists()){
                    codecastRoot.mkdir();
                }

                File codecastProjectRoot = new File(FilePathUtil.getCodeCastProjectRootDirectory(project));
                if(!codecastProjectRoot.exists()){
                    codecastProjectRoot.mkdir();
                }

                System.out.println("Downloading...");
                for(AudioComment comment : comments){
                    try {
                        // Download file to audio folder
                        //Files.copy(new URL(comment.getUrl()).openStream(), Paths.get(FilePathUtil.getCodeCastProjectRootDirectory(project) + comment.getFileName()));
                        downloadFileForComment(project, comment);
                        comment.setDownloaded(true);
                        comment.calculateDuration(project);
                    } catch (FileAlreadyExistsException e){
                        BalloonNotifier.notifyWarning(project, "There is already a file called " + comment.getFileName() + ".");
                    } catch (IOException e) {
                        e.printStackTrace();
                        project.getService(PlayerManagerService.class).notify(new DownloadEvent(DownloadEventType.CANCELED, ""));
                    }
                }
                project.getService(PlayerManagerService.class).notify(new DownloadEvent(DownloadEventType.FINISHED, ""));
            }
        });
    }

    /**
     * returns list with all comments which aren't downloaded yet
     * @param project current project
     * @param comments all comments of playlist
     * @return list with comments to download
     */
    private static ArrayList<AudioComment> getCommentsToDownload(Project project, ArrayList<AudioComment> comments){
        ArrayList<AudioComment> toDownload = new ArrayList<>();
        for(AudioComment comment : comments){
            try {
                if(!FilePathUtil.checkCommentDownloadedUpToDate(project, comment)){
                    toDownload.add(comment);
                }else{
                    comment.setDownloaded(true);
                    comment.calculateDuration(project);
                }
            } catch (NoFileUrlException e) {
                BalloonNotifier.notifyError(project, e.getMessage());
            }
        }
        return toDownload;
    }

    /**
     * from: https://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
     * Downloads a file from a URL
     * @param comment comment to download
     * @param project Project
     * @throws IOException
     */
    public static void downloadFileForComment(Project project, AudioComment comment)
            throws IOException {
        String fileURL = comment.getUrl();
        URL url = new URL(comment.getUrl());
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = FilePathUtil.getFilePathForComment(project, comment);

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
        } else {
            httpConn.disconnect();
            throw new IOException("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }


}
