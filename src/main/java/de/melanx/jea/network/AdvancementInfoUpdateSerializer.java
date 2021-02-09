package de.melanx.jea.network;

import com.google.gson.JsonElement;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.ingredient.AdvancementInfo;
import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.HashSet;
import java.util.Set;

public class AdvancementInfoUpdateSerializer implements PacketSerializer<AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage> {

    @Override
    public Class<AdvancementInfoUpdateMessage> messageClass() {
        return AdvancementInfoUpdateMessage.class;
    }

    @Override
    public void encode(AdvancementInfoUpdateMessage msg, PacketBuffer buffer) {
        buffer.writeVarInt(msg.infos.size());
        msg.infos.forEach(info -> {
            buffer.writeResourceLocation(info.id);
            buffer.writeCompoundTag(info.display.write(new CompoundNBT()));
            buffer.writeTextComponent(info.translation);
            buffer.writeBoolean(info.tooltip != null);
            if (info.tooltip != null) {
                buffer.writeString(JustEnoughAdvancements.GSON.toJson(info.tooltip.serialize()), 0x40000);
            }
        });
    }

    @Override
    public AdvancementInfoUpdateMessage decode(PacketBuffer buffer) {
        Set<AdvancementInfo> infos = new HashSet<>();
        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation id = buffer.readResourceLocation();
            @SuppressWarnings("ConstantConditions")
            ItemStack display = ItemStack.read(buffer.readCompoundTag());
            ITextComponent translation = buffer.readTextComponent();
            ItemPredicate tooltip = null;
            if (buffer.readBoolean()) {
                tooltip = ItemPredicate.deserialize(JustEnoughAdvancements.GSON.fromJson(buffer.readString(0x40000), JsonElement.class));
            }
            infos.add(new AdvancementInfo(id, display, translation, tooltip));
        }

        return new AdvancementInfoUpdateMessage(infos);
    }

    public static class AdvancementInfoUpdateMessage {
        public final Set<AdvancementInfo> infos;

        public AdvancementInfoUpdateMessage(Set<AdvancementInfo> infos) {
            this.infos = infos;
        }
    }
}
