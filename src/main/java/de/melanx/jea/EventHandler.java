package de.melanx.jea;

import de.melanx.jea.config.JeaConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.moddingx.libx.event.ConfigLoadedEvent;

public class EventHandler {
    
    private boolean needsResyncAdvancements = false;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            JustEnoughAdvancements.getNetwork().syncAdvancements(serverPlayer.serverLevel().getServer(), serverPlayer);
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
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (this.needsResyncAdvancements) {
            this.needsResyncAdvancements = false;
            JustEnoughAdvancements.getNetwork().syncAdvancements(event.getServer());
        }
    }
    
    @SubscribeEvent
    public void configReload(ConfigLoadedEvent event) {
        if (event.getConfigClass() == JeaConfig.class && event.getReason() == ConfigLoadedEvent.LoadReason.RELOAD) {
            this.needsResyncAdvancements = true;
        }
    }
}
