package de.melanx.jea.network;

import de.melanx.jea.client.data.ClientAdvancements;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AdvancementInfoUpdateHandler {
    
    public static void handle(AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientAdvancements.update(msg.advancements));
        ctx.get().setPacketHandled(true);
    }
}
