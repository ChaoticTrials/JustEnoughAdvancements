package de.melanx.jea.client;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ClientAdvancementProgress {
    
    @Nullable
    public static AdvancementProgress getProgress(ResourceLocation advancement) {
        return ClientAdvancementProgress.getProgress(Minecraft.getInstance(), advancement);
    }
    
    @Nullable
    public static AdvancementProgress getProgress(Minecraft mc, ResourceLocation advancement) {
        if (mc.getConnection() != null) {
            ClientAdvancements mgr = mc.getConnection().getAdvancements();
            AdvancementHolder clientAdvancement = mgr.get(advancement);
            if (clientAdvancement != null) {
                AdvancementProgress progress = mgr.progress.get(clientAdvancement);
                //noinspection RedundantIfStatement
                if (progress != null) {
                    return progress;
                }
            }
        }

        return null;
    }
}
