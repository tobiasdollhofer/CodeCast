package de.tobiasdollhofer.codecast.player.util.constants;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

/**
 * Interface for icon paths in resource directory
 */
public interface PluginIcons {

    Icon playFirst = IconLoader.getIcon("/icons/play_first.svg", PluginIcons.class);
    Icon playPrevious = IconLoader.getIcon("/icons/play_back.svg", PluginIcons.class);
    Icon play = IconLoader.getIcon("/icons/toolWindowRun.svg", PluginIcons.class);
    Icon pause = IconLoader.getIcon("/icons/pause.svg", PluginIcons.class);
    Icon playNext = IconLoader.getIcon("/icons/play_forward.svg", PluginIcons.class);
    Icon playLast = IconLoader.getIcon("/icons/play_last.svg", PluginIcons.class);
    Icon volume = IconLoader.getIcon("/icons/volume_up.svg", PluginIcons.class);
    Icon volumeOff = IconLoader.getIcon("/icons/volume_off.svg", PluginIcons.class);
    Icon autoPlayOff = IconLoader.getIcon("/icons/infinity.svg", PluginIcons.class);
    Icon autoPlayOn = IconLoader.getIcon("/icons/infinity_on.svg", PluginIcons.class);
    Icon showCodeOff = IconLoader.getIcon("/icons/showCode.svg", PluginIcons.class);
    Icon showCodeOn = IconLoader.getIcon("/icons/showCode_on.svg", PluginIcons.class);
    Icon jumpToCode = IconLoader.getIcon("/icons/stepOutCodeBlock.svg", PluginIcons.class);
    Icon playCodecast = IconLoader.getIcon("/icons/play_codecast.svg", PluginIcons.class);
    Icon refresh = IconLoader.getIcon("/icons/refresh.svg", PluginIcons.class);
}
