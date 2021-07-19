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

        Playlist playlist = new Playlist();

        for(VirtualFile file : codecastFiles){
            addCommentsFromFile(playlist, file, project);
        }

        DownloadUtil.downloadComments(project, playlist);
        System.out.println(playlist);
        // build playlist on using all comments
        return playlist;
    }

    /**
     * Method creates list with all comments found in this file
     * @param file virtual file to look at
     * @return arraylist with comments
     */
    private static Playlist addCommentsFromFile(Playlist playlist, VirtualFile file, Project project) {
        PsiFile psi = PsiManager.getInstance(project).findFile(file);
        @NotNull Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psi, PsiComment.class);
        for(PsiComment comment : psiComments){
            // check if comment is complete
            if(comment.getText().contains(Config.CODECAST_ANNOTATION) && comment.getText().contains(Config.URL_ANNOTATION)){
                addCommentFromTextBlock(comment, playlist);
            }else if(comment.getText().contains(Config.CODECAST_INFO_ANNOTATION)){
                addCodecastInfotoList(comment, playlist);
            }
        }
        return playlist;
    }

    private static void addCodecastInfotoList(PsiComment comment, Playlist playlist) {
        String info = CommentExtractor.getCodecastInfoFromTextBlock(comment);
        playlist.setInformationText(info);
    }

    private static void addCommentFromTextBlock(PsiComment comment, Playlist playlist){
        AudioComment audioComment = CommentExtractor.getCommentFromTextBlock(comment);
        if(audioComment != null){
            playlist.addComment(audioComment);
        }
    }


}
