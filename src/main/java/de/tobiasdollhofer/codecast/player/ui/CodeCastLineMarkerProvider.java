package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.codeInsight.daemon.*;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.refactoring.classMembers.MemberInfoBase;
import com.intellij.refactoring.classMembers.MemberInfoTooltipManager;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.service.PlayerManagerServiceImpl;
import de.tobiasdollhofer.codecast.player.util.JumpToCodeUtil;
import de.tobiasdollhofer.codecast.player.util.PlaylistLoader;
import de.tobiasdollhofer.codecast.player.util.PluginIcons;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.util.Collection;

public class CodeCastLineMarkerProvider implements LineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if(!(element instanceof PsiComment)){
            return null;
        }
        String value = element.getText();
        if(value.contains("@codecast")){
            GutterIconNavigationHandler handler = new GutterIconNavigationHandler() {
                @Override
                public void navigate(MouseEvent e, PsiElement elt) {
                    AudioComment comment = PlaylistLoader.getCommentFromTextBlock(elt.getText());
                    System.out.println(comment);
                    Project project = elt.getProject();
                    project.getService(PlayerManagerService.class).setPlaylistCommentForFoundComment(comment);
                }
            };

            PsiElement elementAfterComment = JumpToCodeUtil.findElementAfterCommentElement(element);
            // TODO: remove deprecated function
            /*return  new LineMarkerInfo(elementAfterComment, elementAfterComment.getTextRange(), PluginIcons.play,
                    str -> "Play CodeCast comment", handler, GutterIconRenderer.Alignment.CENTER);*/
            return  new LineMarkerInfo(element, elementAfterComment.getTextRange(), PluginIcons.playCodecast,
                    str -> "Play CodeCast comment", handler, GutterIconRenderer.Alignment.CENTER);
        }

        return null;

    }
}
