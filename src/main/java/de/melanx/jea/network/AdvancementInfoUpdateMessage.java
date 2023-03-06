package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.client.ClientAdvancements;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public record AdvancementInfoUpdateMessage(Set<AdvancementInfo> advancements) {
    
    public static class Serializer implements PacketSerializer<AdvancementInfoUpdateMessage> {

        @Override
        public Class<AdvancementInfoUpdateMessage> messageClass() {
            return AdvancementInfoUpdateMessage.class;
        }

        @Override
        public void encode(AdvancementInfoUpdateMessage msg, FriendlyByteBuf buffer) {
            buffer.writeVarInt(msg.advancements.size());
            for (AdvancementInfo info : msg.advancements) {
                info.write(buffer);
            }
        }

        @Override
        public AdvancementInfoUpdateMessage decode(FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            Set<AdvancementInfo> advancements = new HashSet<>(size);
            for (int i = 0; i < size; i++) {
                advancements.add(AdvancementInfo.read(buffer));
            }
            return new AdvancementInfoUpdateMessage(advancements);
        }
    }
    
    public static class Handler implements PacketHandler<AdvancementInfoUpdateMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(AdvancementInfoUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ClientAdvancements.update(msg.advancements());
            return true;
        }
    }
}
