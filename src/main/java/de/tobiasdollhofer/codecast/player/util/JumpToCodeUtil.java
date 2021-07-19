package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.MethodSignature;
import com.intellij.util.PsiNavigateUtil;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.util.playlist.CommentExtractor;
import de.tobiasdollhofer.codecast.player.util.playlist.PlaylistLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class JumpToCodeUtil {

    /**
     * Method jumps to position in code where audio comment was extracted from
     * @param comment audio comment to jump to in code
     */
    public static void jumpToCode(Project project, AudioComment comment){
        System.out.println("Jump to comment in code : " + comment);
        // invoke later to prevent "Read access is allowed from event dispatch thread or inside read-action only"-Exception
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                PsiComment target = CommentExtractor.findElementForComment(project, comment);
                if(target != null)
                    PsiNavigateUtil.navigate(findElementAfterCommentElement(target));
            }
        });

    }

    /**
     *
     * @param el PsiElement for comment
     * @param comment audio comment to check for
     * @return if audio comment is stored in this psielement
     */
    private static boolean checkElementContainsComment(PsiElement el, AudioComment comment){
        String text = el.getText().toLowerCase(Locale.ROOT);

        return text.contains(comment.getTitle().toLowerCase(Locale.ROOT)) && text.contains(comment.getUrl().toLowerCase(Locale.ROOT))
                && text.contains(comment.getChapter().toLowerCase(Locale.ROOT));
    }

    /**
     *
     * @param el comment element
     * @return element 2 index positions after (2, because 1 would be end of block comment
     */
    public static PsiElement findElementAfterCommentElement(PsiElement el){
        PsiElement parent = el.getParent();
        PsiElement @NotNull [] children = parent.getChildren();

        // search for element two index positions behind provided element
        for(int i = 0; i < children.length; i++){
            if(children[i].equals(el) && children.length > i+2){

                // target element after comment
                PsiElement targetElement = children[i+2];

                // check if element is a method
                if(targetElement instanceof PsiMethod){

                    // check if there are annotations before method
                    PsiAnnotation[] annotations = ((PsiMethod) targetElement).getAnnotations();
                    if(annotations.length > 0){

                        // get all child elements of target including annotations, method signature and so on
                        PsiElement[] targetChildren = targetElement.getChildren();
                        if(targetChildren.length > 1){
                            // return first child element after annotation
                            return targetChildren[0].getNextSibling();
                        }
                    }
                }
                return targetElement;
            }
        }
        // return last element of file in case all other scenarios didn't fit
        return children[children.length - 1];
    }
}
