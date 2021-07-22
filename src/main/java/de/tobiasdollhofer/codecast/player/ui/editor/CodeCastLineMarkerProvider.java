package de.tobiasdollhofer.codecast.player.ui.editor;

import com.intellij.codeInsight.daemon.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import de.tobiasdollhofer.codecast.player.data.AudioComment;
import de.tobiasdollhofer.codecast.player.service.playermanager.PlayerManagerService;
import de.tobiasdollhofer.codecast.player.util.JumpToCodeUtil;
import de.tobiasdollhofer.codecast.player.util.playlist.CommentExtractor;
import de.tobiasdollhofer.codecast.player.util.constants.Config;
import de.tobiasdollhofer.codecast.player.util.constants.PluginIcons;
import de.tobiasdollhofer.codecast.player.util.constants.Strings;
import org.jetbrains.annotations.NotNull;

/**
 * creates gutter icons next to line number
 * provides play-pause directly in editor
 */
public class CodeCastLineMarkerProvider implements LineMarkerProvider {

    /**
     *
     * @param element element for which line marker info should be created
     * @return line marker info object or null if element is no codecast comment
     */
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if(!(element instanceof PsiComment)){
            return null;
        }
        String value = element.getText();
        if(value.contains(Config.CODECAST_ANNOTATION)){
            GutterIconNavigationHandler handler = (e, elt) -> {
                AudioComment comment = CommentExtractor.getCommentFromTextBlock(elt.getText());
                Project project = elt.getProject();
                project.getService(PlayerManagerService.class).setPlaylistCommentForFoundComment(comment);
            };

            PsiElement elementAfterComment = JumpToCodeUtil.findElementAfterCommentElement(element);
            return  new LineMarkerInfo(element, elementAfterComment.getTextRange(), PluginIcons.playCodecast,
                    str -> Strings.TOOLTIP_GUTTER_ICON, handler, GutterIconRenderer.Alignment.CENTER);
        }
        return null;

    }
}
