package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Creates automatic folding of codecast comments
 */
public class CodeCastFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    /**
     * Method creates descriptor for codecast folding (replacement with placeholdertext)
     * @param root element to search in
     * @param document
     * @param quick
     * @return
     */
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        // Initialize the group of folding regions that will expand/collapse together.
        FoldingGroup group = FoldingGroup.newGroup("CodeCast");
        // Initialize the list of folding regions
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        // search for comments in this root element
        Collection<PsiComment> comments =
                PsiTreeUtil.findChildrenOfType(root, PsiComment.class);
        // check in each comment if it is a codecast comment
        for (final PsiComment comment : comments) {
            String value = comment.getText();
            // add descriptor if it is a codecast comment
            if (value != null && value.contains("@codecast")) {
                descriptors.add(new FoldingDescriptor(comment.getNode(),
                        new TextRange(comment.getTextRange().getStartOffset() + 1, comment.getTextRange().getEndOffset() - 1), group));
            }
        }
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    /**
     *
     * @param node node where text will be displayed
     * @return placeholder text for folded comments
     */
    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        return "codecast";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
