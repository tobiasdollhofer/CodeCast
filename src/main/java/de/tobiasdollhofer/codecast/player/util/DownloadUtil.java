package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerServiceImpl;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEvent;
import de.tobiasdollhofer.codecast.player.util.event.downloader.DownloadEventType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DownloadUtil {

    public static void downloadComments(Project project, Playlist playlist){
        ArrayList<AudioComment> comments = getCommentsToDownload(project, playlist.getAllComments());

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Downloading CodeCast Audiofiles...") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                System.out.println("Downloading...");
                for(AudioComment comment : comments){
                    System.out.println(comment);
                    try {
                        Files.copy(new URL(comment.getUrl()).openStream(), Paths.get(FilePathUtil.getCodeCastAudioDirectory(project) + comment.getFileName()));
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

    private static ArrayList<AudioComment> getCommentsToDownload(Project project, ArrayList<AudioComment> comments){
        ArrayList<AudioComment> toDownload = new ArrayList<>();
        for(AudioComment comment : comments){
            try {
                if(!FilePathUtil.checkCommentDownloaded(project, comment)){
                    toDownload.add(comment);
                }else{
                    comment.setDownloaded(true);
                }
            } catch (NoFileUrlException e) {
                BalloonNotifier.notifyError(project, e.getMessage());
            }
        }
        return toDownload;
    }

}
