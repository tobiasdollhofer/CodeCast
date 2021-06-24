package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEvent;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEventType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DownloadUtil {

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
                System.out.println("Downloading...");
                for(AudioComment comment : comments){
                    try {
                        // Download file to audio folder
                        Files.copy(new URL(comment.getUrl()).openStream(), Paths.get(FilePathUtil.getCodeCastAudioDirectory(project) + comment.getFileName()));
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
                if(!FilePathUtil.checkCommentDownloaded(project, comment)){
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

}
