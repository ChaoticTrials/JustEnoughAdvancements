package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class ChangeDimensionSerializer extends CriterionSerializer<ChangeDimensionTrigger.Instance> {

    public ChangeDimensionSerializer() {
        super(ChangeDimensionTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANGE_DIMENSION);
    }

    @Override
    public void write(ChangeDimensionTrigger.Instance instance, PacketBuffer buffer) {
        byte mask = 0;
        if (instance.from != null) mask |= 1;
        if (instance.to != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (instance.from != null) {
            PacketUtil.writeKey(instance.from, buffer);
        }
        if (instance.to != null) {
            PacketUtil.writeKey(instance.to, buffer);
        }
    }

    @Override
    public ChangeDimensionTrigger.Instance read(PacketBuffer buffer) {
        int mask = buffer.readByte();
        RegistryKey<World> from = null;
        RegistryKey<World> to = null;
        if ((mask & 1) != 0) {
            from = PacketUtil.readKey(buffer, World.class);
        }
        if ((mask & (1 << 1)) != 0) {
            to = PacketUtil.readKey(buffer, World.class);
        }
        return new ChangeDimensionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, from, to);
    }
}
