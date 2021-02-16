package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class PlaceBlockSerializer extends CriterionSerializer<PlacedBlockTrigger.Instance> {

    public PlaceBlockSerializer() {
        super(PlacedBlockTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.PLACE_BLOCK);
    }

    @Override
    public void write(PlacedBlockTrigger.Instance instance, PacketBuffer buffer) {
        byte mask = 0;
        if (instance.block != null) mask |= 1;
        if (instance.properties != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (instance.block != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.block.getRegistryName()));
        }
        if (instance.properties != null) {
            PacketUtil.writeStatePropertiesPredicate(instance.properties, buffer);
        }
        PacketUtil.writeLocationPredicate(instance.location, buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public PlacedBlockTrigger.Instance read(PacketBuffer buffer) {
        byte mask = buffer.readByte();
        Block block = null;
        StatePropertiesPredicate properties = null;
        if ((mask & 1) != 0) {
            block = ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
        }
        if ((mask & (1 << 1)) != 0) {
            properties = PacketUtil.readStatePropertiesPredicate(buffer);
        }
        LocationPredicate location = PacketUtil.readLocationPredicate(buffer);
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        //noinspection ConstantConditions
        return new PlacedBlockTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, block, properties, location, item);
    }
}
