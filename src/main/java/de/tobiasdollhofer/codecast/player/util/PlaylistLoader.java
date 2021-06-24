package de.tobiasdollhofer.codecast.player.util;

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
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project));

        // find all files with @codecast annotation
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);

        // extract all comments from codecast files
        ArrayList<AudioComment> comments = new ArrayList<>();
        for(VirtualFile file : codecastFiles){
            comments.addAll(getCommetsFromFile(file, project));
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
    private static ArrayList<AudioComment> getCommetsFromFile(VirtualFile file, Project project) {
        ArrayList<AudioComment> comments = new ArrayList<>();
        PsiFile psi = PsiManager.getInstance(project).findFile(file);
        @NotNull Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psi, PsiComment.class);
        for(PsiComment comment : psiComments){
            // check if comment is complete
            if(comment.getText().contains("@codecast") && comment.getText().contains("@url")){
                addCommentFromTextBlock(project, comment, psi, comments);
            }
        }
        return comments;
    }

    private static void addCommentFromTextBlock(Project project, PsiComment comment, PsiFile file, ArrayList<AudioComment> list){

        AudioComment audioComment = getCommentFromTextBlock(project, comment.getText(), file);

        if(audioComment != null){
            list.add(audioComment);
        }
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @param text textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    private static AudioComment getCommentFromTextBlock(Project project, String text, PsiFile file){
        AudioComment comment = getCommentFromTextBlock(text);
        if(comment != null){
            comment.setFile(file);
        }
        return comment;
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @param textBlock textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    public static AudioComment getCommentFromTextBlock(String textBlock){
        String rawInfos = getValueAfterAnnotation("@codecast", textBlock);
        String url = getValueAfterAnnotation("@url", textBlock);

        // return if no values for annotations were found
        if(rawInfos.equals("") || url.equals("")) return null;

        // split codecast-infos on pipe -> ["X. Chapter title", " X. comment title (type)"]
        String[] rawInfoSplit = rawInfos.split("\\|");

        // check if info is complete
        if(rawInfoSplit.length != 2) return null;

        String chapter = rawInfoSplit[0].trim();

        // split before comment type -> ["X. comment title", "type)"]
        String[] rawTitleSplit = rawInfoSplit[1].split("\\(");
        String title = rawTitleSplit[0].trim();

        AudioCommentType type;
        try{
            // take everything from after the opening bracket
            String rawCommentValue = rawTitleSplit[1];
            // remove closing bracket and get depending comment type: "type)" -> AudioCommentType.TYPE
            type = AudioCommentType.valueOf(rawCommentValue.replace("\\)", "").trim().toUpperCase(Locale.ROOT));
        }catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e){
            // use default type if annotated type was not found or no type was annotated
            type = AudioCommentType.DEFAULT;
        }

        AudioComment comment = new AudioComment(title, type);
        comment.setChapter(chapter);
        comment.setUrl(url);
        return comment;
    }

    /**
     * Method extracts value after annotation in a string
     * @param annotation Annotation which value has to be extracted
     * @param text text to search for value of annotation
     * @return string value
     */
    private static String getValueAfterAnnotation(String annotation, String text){
        String value = "";

        // cut off whole text before the actual value of the annotation
        String rawValue = text.substring(text.indexOf(annotation) + annotation.length());

        // cut off whole text after linebreak
        rawValue = rawValue.split("\\r?\\n")[0];

        // remove leading whitespaces
        value = rawValue.trim();
        return value;
    }

    /**
     * Method searches for files with codecast annotations
     * @param javaFiles collection of virtual javafiles
     * @return arraylist with virtualfiles with codecast annotations
     */
    private static ArrayList<VirtualFile> getAllCodecastFiles(Collection<VirtualFile> javaFiles){
        ArrayList<VirtualFile> codecastFiles = new ArrayList<>();

        // check for each file if there is a codecast annotation in there
        for(VirtualFile file : javaFiles){
            CharSequence text = LoadTextUtil.loadText(file);
            String textString = text.toString();

            if(textString.contains("@codecast")){
                codecastFiles.add(file);
            }
        }
        return codecastFiles;
    }
}
