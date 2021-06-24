package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PsiNavigateUtil;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;

public class JumpToCodeUtil {

    /**
     * Method jumps to position in code where audio comment was extracted from
     * @param comment audio comment to jump to in code
     */
    public static void jumpToCode(AudioComment comment){
        System.out.println("Jump to comment in code : " + comment);
        // invoke later to prevent "Read access is allowed from event dispatch thread or inside read-action only"-Exception
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                PsiFile commentFile = comment.getFile();
                @NotNull Collection<PsiComment> element = PsiTreeUtil.findChildrenOfType(commentFile, PsiComment.class);
                for(PsiElement el : element){
                    if(checkElementContainsComment(el, comment)){
                        // jump to end of comment and therefore to the beginning of the actual method where the audiocomment is for
                        PsiElement target = findElementAfterCommentElement(el);
                        PsiNavigateUtil.navigate(target);
                        return;
                    }
                }
                System.out.println("Jump to code not possible: Comment not found!");
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
        for(int i = 0; i < children.length; i++){
            if(children[i].equals(el)){
                if(children.length > i+2){
                    return children[i+2];
                }
            }
        }
        return children[children.length - 1];
    }
}
