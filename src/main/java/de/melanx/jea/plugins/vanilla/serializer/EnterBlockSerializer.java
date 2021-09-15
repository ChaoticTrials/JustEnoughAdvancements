package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class EnterBlockSerializer extends CriterionSerializer<EnterBlockTrigger.TriggerInstance> {

    public EnterBlockSerializer() {
        super(EnterBlockTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.ENTER_BLOCK);
    }

    @Override
    public void write(EnterBlockTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        byte mask = 0;
        if (instance.block != null) mask |= 1;
        if (instance.state != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (instance.block != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.block.getRegistryName()));
        }
        if (instance.state != null) {
            PacketUtil.writeStatePropertiesPredicate(instance.state, buffer);
        }
    }

    @Override
    public EnterBlockTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        byte mask = buffer.readByte();
        Block block = null;
        StatePropertiesPredicate properties = null;
        if ((mask & 1) != 0) {
            block = ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
        }
        if ((mask & (1 << 1)) != 0) {
            properties = PacketUtil.readStatePropertiesPredicate(buffer);
        }
        //noinspection ConstantConditions
        return new EnterBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, block, properties);
    }
}
