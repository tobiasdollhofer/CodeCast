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
import de.tobiasdollhofer.codecast.player.util.constants.Config;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Util class used to extract AudioComment-Object from comment and to extract codecast info String used on the bottom of the player UI
 * Also used to find a psicomment for an audio comment object
 */
public class CommentExtractor {

    /**
     *
     * @param psiComment comment with information
     * @return text representation with information about codecast project
     */
    public static String getCodecastInfoFromTextBlock(PsiComment psiComment){
        return getCodecastInfoFromTextBlock(psiComment.getText());
    }

    /**
     *
     * @param textBlock comment with information
     * @return text representation with information about codecast project
     */
    public static String getCodecastInfoFromTextBlock(String textBlock){
        return getValueAfterAnnotation(Config.CODECAST_INFO_ANNOTATION, textBlock, true);
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    public static AudioComment getCommentFromTextBlock(PsiComment psiComment){
        return getCommentFromTextBlock(psiComment.getText());
    }

    /**
     * Method creates single content from a textblock which codecast-comment completeness was already checked
     * @param textBlock textblock which codecast-comment completeness was already checked
     * @return single audio comment
     */
    public static AudioComment getCommentFromTextBlock(String textBlock){
        String rawInfos = getValueAfterAnnotation(Config.CODECAST_ANNOTATION, textBlock);
        String url = getValueAfterAnnotation(Config.URL_ANNOTATION, textBlock);

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
            type = AudioCommentType.valueOf(rawCommentValue.replace(")", "").trim().toUpperCase(Locale.ROOT));
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
     * @param ignoreLinebreak ignore linebreaks while extracting
     * @return string value
     */
    private static String getValueAfterAnnotation(String annotation, String text, boolean ignoreLinebreak){
        String value = "";

        if(text.equals("") || text.length() < annotation.length()) return value;
        // cut off whole text before the actual value of the annotation
        String rawValue = text.substring(text.indexOf(annotation) + annotation.length());

        if(!ignoreLinebreak){
            // cut off whole text after linebreak
            rawValue = rawValue.split("\\r?\\n")[0];
        }else {
            rawValue = rawValue.split("\\*/")[0];
        }

        // remove leading whitespaces
        value = rawValue.trim();
        return value;
    }

    /**
     * Method extracts value after annotation in a string
     * @param annotation Annotation which value has to be extracted
     * @param text text to search for value of annotation
     * @return string value
     */
    private static String getValueAfterAnnotation(String annotation, String text){
        return getValueAfterAnnotation(annotation, text, false);
    }

    /**
     * Method searches for files with codecast annotations
     * @param javaFiles collection of virtual javafiles
     * @return arraylist with virtualfiles with codecast annotations
     */
    public static ArrayList<VirtualFile> getAllCodecastFiles(Collection<VirtualFile> javaFiles){
        ArrayList<VirtualFile> codecastFiles = new ArrayList<>();

        // check for each file if there is a codecast annotation in there
        for(VirtualFile file : javaFiles){
            CharSequence text = LoadTextUtil.loadText(file);
            String textString = text.toString();

            if(textString.contains(Config.CODECAST_ANNOTATION)){
                codecastFiles.add(file);
            }
        }
        return codecastFiles;
    }

    /**
     * method searches psicomment element in the current project where a specific audio comment belongs to
     * @param project current project
     * @param comment AudioComment object for which the psielement is searched for
     * @return comment element
     */
    public static PsiComment findElementForComment(Project project, AudioComment comment) {
        // find all java project files
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, Config.LANGUAGE, GlobalSearchScope.projectScope(project));
        // find all files with @codecast annotation
        ArrayList<VirtualFile> codecastFiles = getAllCodecastFiles(files);

        return findElementInCodeCastFiles(project, codecastFiles, comment);
    }

    /**
     * method searches psicomment element in all files with codecast annotations where a specific audio comment belongs to
     * @param project current project
     * @param codecastFiles list of files containing @codecast annotation
     * @param comment AudioComment object for which the psielement is searched for
     * @return comment element
     */
    private static PsiComment findElementInCodeCastFiles(Project project, ArrayList<VirtualFile> codecastFiles, AudioComment comment) {
        for(VirtualFile file : codecastFiles){
            PsiFile psi = PsiManager.getInstance(project).findFile(file);
            @NotNull Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(psi, PsiComment.class);

            for(PsiComment psiComment : comments){
                if(comment.equals(getCommentFromTextBlock(psiComment))){
                    return psiComment;
                }
            }
        }
        return null;
    }

}
