package de.melanx.jea;

import io.github.noeppi_noeppi.libx.event.DatapacksReloadedEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity && event.getPlayer().world.getServer() != null) {
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.getPlayer().world.getServer(), (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void resourcesReload(DatapacksReloadedEvent event) {
        JustEnoughAdvancements.getNetwork().syncAdvancements(event.getServer());
    }
}
