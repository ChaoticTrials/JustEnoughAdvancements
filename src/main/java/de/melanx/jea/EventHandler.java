package de.melanx.jea;

import de.melanx.jea.config.JeaConfig;
import io.github.noeppi_noeppi.libx.event.ConfigLoadedEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    
    private boolean needsResyncAdvancements = false;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer && event.getPlayer().level.getServer() != null) {
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.getPlayer().level.getServer(), (ServerPlayer) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void resourcesReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.getPlayerList().getServer());
        } else {
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.getPlayerList().getServer(), event.getPlayer());
        }
    }
    
    @SubscribeEvent
    public void serverTick(TickEvent.WorldTickEvent event) {
        // Reason that this is not a ServerTickEvent is that we need a MinecraftServer instance.
        if (this.needsResyncAdvancements && event.world instanceof ServerLevel) {
            this.needsResyncAdvancements = false;
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.world.getServer());
        }
    }
    
    @SubscribeEvent
    public void configReload(ConfigLoadedEvent event) {
        if (event.getConfigClass() == JeaConfig.class && event.getReason() == ConfigLoadedEvent.LoadReason.RELOAD) {
            this.needsResyncAdvancements = true;
        }
    }
}
