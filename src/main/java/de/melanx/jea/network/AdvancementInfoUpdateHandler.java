package de.melanx.jea.network;

import de.melanx.jea.util.ClientAdvancementInfo;
import mezz.jei.startup.JeiReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.function.Supplier;

public class AdvancementInfoUpdateHandler {
    public static void handle(AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            msg.infos.forEach(info -> {
                ClientAdvancementInfo.updateAdvancementInfo(info.id, info.display, info.translation, info.tooltip);
            });
            if (Minecraft.getInstance().getResourceManager() instanceof SimpleReloadableResourceManager) {
                SimpleReloadableResourceManager resourceManager = (SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager();
                for (IFutureReloadListener listener : resourceManager.reloadListeners) {
                    if (listener instanceof JeiReloadListener) {
                        ((ISelectiveResourceReloadListener) listener).onResourceManagerReload(resourceManager); // TODO
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
