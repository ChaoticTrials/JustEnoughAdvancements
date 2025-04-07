package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.client.ClientAdvancements;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.network.PacketHandler;

import java.util.HashSet;
import java.util.Set;

public class AdvancementInfoUpdateHandler extends PacketHandler<AdvancementInfoUpdateHandler.Message> {

    public static final CustomPacketPayload.Type<AdvancementInfoUpdateHandler.Message> TYPE = new CustomPacketPayload.Type<>(JustEnoughAdvancements.getInstance().resource("advancement_info_update"));

    public AdvancementInfoUpdateHandler() {
        super(TYPE, PacketFlow.CLIENTBOUND, Message.CODEC, HandlerThread.MAIN);
    }

    @Override
    public void handle(Message msg, IPayloadContext ctx) {
        ClientAdvancements.update(msg.advancements());
    }

    public record Message(Set<AdvancementInfo> advancements) implements CustomPacketPayload {

        public static final StreamCodec<RegistryFriendlyByteBuf, Message> CODEC = StreamCodec.of(
                (buffer, value) -> {
                    buffer.writeVarInt(value.advancements.size());
                    for (AdvancementInfo info : value.advancements) {
                        info.write(buffer);
                    }
                }, buffer -> {
                    int size = buffer.readVarInt();
                    Set<AdvancementInfo> advancements = new HashSet<>(size);
                    for (int i = 0; i < size; i++) {
                        advancements.add(AdvancementInfo.read(buffer));
                    }

                    return new Message(advancements);
                }
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return AdvancementInfoUpdateHandler.TYPE;
        }
    }
}
