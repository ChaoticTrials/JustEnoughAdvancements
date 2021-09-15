package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashSet;
import java.util.Set;

public class AdvancementInfoUpdateSerializer implements PacketSerializer<AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage> {

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

    public record AdvancementInfoUpdateMessage(Set<AdvancementInfo> advancements) {}
}
