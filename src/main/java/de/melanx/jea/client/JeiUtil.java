package de.melanx.jea.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public class JeiUtil {
    
    public static void triggerReload() {
        try {
            if (Minecraft.getInstance().getResourceManager() instanceof SimpleReloadableResourceManager) {
                SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager();
                Class<?> c = Class.forName("mezz.jei.startup.ClientLifecycleHandler$JeiReloadListener");
                for (IFutureReloadListener listener : resourceManager.reloadListeners) {
                    if (listener instanceof ISelectiveResourceReloadListener && c.isInstance(listener)) {
                        ((ISelectiveResourceReloadListener) listener).onResourceManagerReload(resourceManager);
                    }
                }
            }
        } catch (ReflectiveOperationException | NoClassDefFoundError e) {
            //
        }
    }
}
