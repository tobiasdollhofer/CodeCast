package de.tobiasdollhofer.codecast.player.util.playlist;

import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.data.AudioCommentType;
import de.tobiasdollhofer.codecast.player.data.Playlist;
import de.tobiasdollhofer.codecast.player.util.DownloadUtil;
import de.tobiasdollhofer.codecast.player.util.constants.Config;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class PlaylistLoader {



    /**
     * Method generates Playlist from project comment annotations
     * @param project current project
     * @return Playlist
     */
    public static Playlist loadPlaylistFromComments(Project project) {

        // find all java project files
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, Config.LANGUAGE, GlobalSearchScope.projectScope(project));

        // find all files with @codecast annotation
        ArrayList<VirtualFile> codecastFiles = CommentExtractor.getAllCodecastFiles(files);

        // extract all comments from codecast files
        ArrayList<AudioComment> comments = new ArrayList<>();
        for(VirtualFile file : codecastFiles){
            comments.addAll(getCommentsFromFile(file, project));
        }

        // build playlist on using all comments
        return createPlaylistFromComments(project, comments);
    }

    /**
     * Method generates playlist from provided comments list
     * @param comments extracted comments list
     * @return playlist
     */
    private static Playlist createPlaylistFromComments(Project project, ArrayList<AudioComment> comments) {
        Playlist playlist = new Playlist();

        // add all comments to playlist (sorting will be handled by playlist and their chapters)
        for(AudioComment comment : comments){
            playlist.addComment(comment);
        }

        DownloadUtil.downloadComments(project, playlist);
        System.out.println(playlist);
        return playlist;
    }

    /**
     * Method creates list with all comments found in this file
     * @param file virtual file to look at
     * @return arraylist with comments
     */
    private static ArrayList<AudioComment> getCommentsFromFile(VirtualFile file, Project project) {
        ArrayList<AudioComment> comments = new ArrayList<>();
        PsiFile psi = PsiManager.getInstance(project).findFile(file);
        @NotNull Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psi, PsiComment.class);
        for(PsiComment comment : psiComments){
            // check if comment is complete
            if(comment.getText().contains(Config.CODECAST_ANNOTATION) && comment.getText().contains(Config.URL_ANNOTATION)){
                addCommentFromTextBlock(project, comment, psi, comments);
            }
        }
        return comments;
    }

    private static void addCommentFromTextBlock(Project project, PsiComment comment, PsiFile file, ArrayList<AudioComment> list){

        AudioComment audioComment = CommentExtractor.getCommentFromTextBlock(comment);

        if(audioComment != null){
            list.add(audioComment);
        }
    }


}
