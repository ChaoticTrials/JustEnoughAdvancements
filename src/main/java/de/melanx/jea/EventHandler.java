package de.melanx.jea;

import mezz.jei.api.JeiPlugin;
import mezz.jei.startup.JeiReloadListener;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = "jea")
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity && event.getPlayer().getEntityWorld().getServer() != null) {
            Collection<Advancement> advancements = event.getPlayer().getEntityWorld().getServer().getAdvancementManager().getAllAdvancements();
            JustEnoughAdvancements.getNetwork().syncAdvancementTo(advancements, (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void resourcesReload(AddReloadListenerEvent event) {
        Collection<Advancement> advancements = event.getDataPackRegistries().getAdvancementManager().getAllAdvancements();
        JustEnoughAdvancements.getNetwork().syncAdvancement(advancements);
    }
}
