package de.melanx.jea.client;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ClientAdvancementProgress {
    
    @Nullable
    public static AdvancementProgress getProgress(ResourceLocation advancement) {
        return getProgress(Minecraft.getInstance(), advancement);
    }
    
    @Nullable
    public static AdvancementProgress getProgress(Minecraft mc, ResourceLocation advancement) {
        if (mc.getConnection() != null) {
            ClientAdvancementManager mgr = mc.getConnection().getAdvancementManager();
            Advancement clientAdvacement = mgr.getAdvancementList().getAdvancement(advancement);
            if (clientAdvacement != null) {
                AdvancementProgress progress = mgr.advancementToProgress.get(clientAdvacement);
                //noinspection RedundantIfStatement
                if (progress != null) {
                    return progress;
                }
            }
        }
        return null;
    }
}
