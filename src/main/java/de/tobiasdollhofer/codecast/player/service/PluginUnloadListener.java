package de.tobiasdollhofer.codecast.player.service;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import de.tobiasdollhofer.codecast.player.util.FilePathUtil;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class PluginUnloadListener implements DynamicPluginListener {

    /**
     * removes all audio files if plugin will be uninstalled
     * @param pluginDescriptor
     * @param isUpdate
     */
    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        DynamicPluginListener.super.beforePluginUnload(pluginDescriptor, isUpdate);
        System.out.println("Unloading Plugin - Removing all codecast-files!");
        if(!isUpdate){
            File codeCastDir = new File(FilePathUtil.getCodeCastRootDirectory());
            if(codeCastDir.exists()){
                try {
                    FileUtils.deleteDirectory(codeCastDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
