package de.tobiasdollhofer.codecast.player.ui.editor;

import com.intellij.codeInsight.daemon.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.service.playermanager.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.JumpToCodeUtil;
import de.tobiasdollhofer.codecast.player.util.playlist.CommentExtractor;
import de.tobiasdollhofer.codecast.player.util.playlist.PlaylistLoader;
import de.tobiasdollhofer.codecast.player.util.constants.Config;
import de.tobiasdollhofer.codecast.player.util.constants.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;

public class CodeCastLineMarkerProvider implements LineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if(!(element instanceof PsiComment)){
            return null;
        }
        String value = element.getText();
        if(value.contains(Config.CODECAST_ANNOTATION)){
            GutterIconNavigationHandler handler = new GutterIconNavigationHandler() {
                @Override
                public void navigate(MouseEvent e, PsiElement elt) {
                    AudioComment comment = CommentExtractor.getCommentFromTextBlock(elt.getText());
                    //System.out.println(comment);
                    Project project = elt.getProject();
                    project.getService(PlayerManagerService.class).setPlaylistCommentForFoundComment(comment);
                }
            };

            PsiElement elementAfterComment = JumpToCodeUtil.findElementAfterCommentElement(element);
            // TODO: remove deprecated function
            return  new LineMarkerInfo(element, elementAfterComment.getTextRange(), PluginIcons.playCodecast,
                    str -> Strings.TOOLTIP_GUTTER_ICON, handler, GutterIconRenderer.Alignment.CENTER);
        }

        return null;

    }
}
