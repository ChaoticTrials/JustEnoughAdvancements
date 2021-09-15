package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class ChangeDimensionSerializer extends CriterionSerializer<ChangeDimensionTrigger.TriggerInstance> {

    public ChangeDimensionSerializer() {
        super(ChangeDimensionTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANGE_DIMENSION);
    }

    @Override
    public void write(ChangeDimensionTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
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
    public ChangeDimensionTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        int mask = buffer.readByte();
        ResourceKey<Level> from = null;
        ResourceKey<Level> to = null;
        if ((mask & 1) != 0) {
            from = PacketUtil.readKey(buffer, Level.class);
        }
        if ((mask & (1 << 1)) != 0) {
            to = PacketUtil.readKey(buffer, Level.class);
        }
        return new ChangeDimensionTrigger.TriggerInstance(EntityPredicate.Composite.ANY, from, to);
    }
}
