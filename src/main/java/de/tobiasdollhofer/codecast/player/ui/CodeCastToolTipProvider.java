package de.tobiasdollhofer.codecast.player.ui;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.refactoring.classMembers.MemberInfoBase;
import com.intellij.refactoring.classMembers.MemberInfoTooltipManager;

public class CodeCastToolTipProvider implements MemberInfoTooltipManager.TooltipProvider {

    @Override
    public @NlsContexts.Tooltip String getTooltip(MemberInfoBase memberInfo) {
        return "Play CodeCast comment";
    }

}
