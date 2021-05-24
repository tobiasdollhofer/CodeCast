package de.tobiasdollhofer.codecast.player.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface PluginIcons {

    Icon playFirst = IconLoader.getIcon("/icons/play_first.svg", PluginIcons.class);
    Icon playPrevious = IconLoader.getIcon("/icons/play_back.svg", PluginIcons.class);
    Icon play = IconLoader.getIcon("/icons/toolWindowRun.svg", PluginIcons.class);
    Icon pause = IconLoader.getIcon("/icons/pause.svg", PluginIcons.class);
    Icon playNext = IconLoader.getIcon("/icons/play_forward.svg", PluginIcons.class);
    Icon playLast = IconLoader.getIcon("/icons/play_last.svg", PluginIcons.class);
    Icon volume = IconLoader.getIcon("/icons/volume_up.svg", PluginIcons.class);
    Icon volumeOff = IconLoader.getIcon("/icons/volume_off.svg", PluginIcons.class);
}
