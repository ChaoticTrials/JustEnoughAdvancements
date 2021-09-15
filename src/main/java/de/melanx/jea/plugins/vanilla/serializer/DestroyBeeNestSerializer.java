package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class DestroyBeeNestSerializer extends CriterionSerializer<BeeNestDestroyedTrigger.TriggerInstance> {
    
    public DestroyBeeNestSerializer() {
        super(BeeNestDestroyedTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.DESTROY_BEE_NEST);
    }

    @Override
    public void write(BeeNestDestroyedTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        buffer.writeBoolean(instance.block != null);
        if (instance.block != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.block.getRegistryName()));
        }
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeIntRange(instance.numBees, buffer);
    }

    @Override
    public BeeNestDestroyedTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        Block block = buffer.readBoolean() ? ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation()) : null;
        ItemPredicate itemPredicate = PacketUtil.readItemPredicate(buffer);
        MinMaxBounds.Ints beesContained = PacketUtil.readIntRange(buffer);
        return new BeeNestDestroyedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, block, itemPredicate, beesContained);
    }
}
