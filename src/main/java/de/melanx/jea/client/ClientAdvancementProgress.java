package de.melanx.jea.client;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ClientAdvancementProgress {
    
    @Nullable
    public static AdvancementProgress getProgress(ResourceLocation advancement) {
        return getProgress(Minecraft.getInstance(), advancement);
    }
    
    @Nullable
    public static AdvancementProgress getProgress(Minecraft mc, ResourceLocation advancement) {
        if (mc.getConnection() != null) {
            ClientAdvancements mgr = mc.getConnection().getAdvancements();
            Advancement clientAdvancement = mgr.getAdvancements().get(advancement);
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
