package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class DestroyBeeNestSerializer extends CriterionSerializer<BeeNestDestroyedTrigger.Instance> {
    
    public DestroyBeeNestSerializer() {
        super(BeeNestDestroyedTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.DESTROY_BEE_NEST);
    }

    @Override
    public void write(BeeNestDestroyedTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeBoolean(instance.block != null);
        if (instance.block != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.block.getRegistryName()));
        }
        PacketUtil.writeItemPredicate(instance.itemPredicate, buffer);
        PacketUtil.writeIntRange(instance.beesContained, buffer);
    }

    @Override
    public BeeNestDestroyedTrigger.Instance read(PacketBuffer buffer) {
        Block block = buffer.readBoolean() ? ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation()) : null;
        ItemPredicate itemPredicate = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.IntBound beesContained = PacketUtil.readIntRange(buffer);
        return new BeeNestDestroyedTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, block, itemPredicate, beesContained);
    }
}
