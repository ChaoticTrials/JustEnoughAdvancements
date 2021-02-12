package de.melanx.jea.client;

import mezz.jei.startup.JeiReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class JeiUtil {
    
    public static void triggerReload() {
        if (Minecraft.getInstance().getResourceManager() instanceof SimpleReloadableResourceManager) {
            SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager();
            for (IFutureReloadListener listener : resourceManager.reloadListeners) {
                if (listener instanceof JeiReloadListener) {
                    ((ISelectiveResourceReloadListener) listener).onResourceManagerReload(resourceManager);
                }
            }
        }
    }
}
