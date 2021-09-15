package de.melanx.jea.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.lang.reflect.Method;

public class JeiUtil {
    
    @SuppressWarnings("deprecation")
    public static void triggerReload() {
        try {
            Class<?> internalClass = Class.forName("mezz.jei.Internal");
            Method getReloadListener = internalClass.getDeclaredMethod("getReloadListener");
            getReloadListener.setAccessible(true);
            Object reloadListener = getReloadListener.invoke(null);
            if (reloadListener instanceof ResourceManagerReloadListener listener) {
                listener.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
            }
        } catch (ReflectiveOperationException | NoClassDefFoundError e) {
            //
        }
    }
}
