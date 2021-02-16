package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class SlideBlockSerializer extends CriterionSerializer<SlideDownBlockTrigger.Instance> {

    public SlideBlockSerializer() {
        super(SlideDownBlockTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.SLIDE_BLOCK);
    }

    @Override
    public void write(SlideDownBlockTrigger.Instance instance, PacketBuffer buffer) {
        byte mask = 0;
        if (instance.block != null) mask |= 1;
        if (instance.stateCondition != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (instance.block != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.block.getRegistryName()));
        }
        if (instance.stateCondition != null) {
            PacketUtil.writeStatePropertiesPredicate(instance.stateCondition, buffer);
        }
    }

    @Override
    public SlideDownBlockTrigger.Instance read(PacketBuffer buffer) {
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
        return new SlideDownBlockTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, block, properties);
    }
}
