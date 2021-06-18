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

public class JumpToCodeUtil {

    public static void jumpToCode(Project project, AudioComment comment){
        System.out.println("Jump to comment in code : " + comment);
        // invoke later to prevent "Read access is allowed from event dispatch thread or inside read-action only"-Exception
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                PsiFile nav = comment.getFile();
                //PsiElement[] elements = nav.getChildren();
                // TODO: use this function to extract comments for playlist generation
                @NotNull Collection<PsiComment> element = PsiTreeUtil.findChildrenOfType(nav, PsiComment.class);
                for(PsiElement el : element){
                    System.out.println("Element:" + el.getText());
                    // TODO: validate all fields of comment
                    if(el.getText().contains(comment.getTitle())){
                        PsiNavigateUtil.navigate(el);
                    }
                }
                /**
                 if(element != null){
                 System.out.println("Element found");
                 PsiNavigateUtil.navigate(element);
                 }*/
        /*//PsiRecursiveElementVisitor.EMPTY_VISITOR.visitComment();
        for(PsiElement el : elements){

            System.out.println("Element: " + el.getText());
            int position = el.getText().lastIndexOf(comment.getTitle());
            //PsiNavigateUtil.navigate(el.);

        }*/
            }
        });

    }
}
