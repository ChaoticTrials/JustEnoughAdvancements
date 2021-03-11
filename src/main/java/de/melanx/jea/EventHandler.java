package de.melanx.jea;

import de.melanx.jea.render.FakeClientPlayer;
import io.github.noeppi_noeppi.libx.event.DatapacksReloadedEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
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
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void renderNameplate(RenderNameplateEvent event) {
        if (event.getEntity() instanceof FakeClientPlayer) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
            event.setResult(Event.Result.DENY);
        }
    }
}
