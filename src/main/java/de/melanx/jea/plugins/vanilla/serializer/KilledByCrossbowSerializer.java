package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.util.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledByCrossbowTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.PacketBuffer;

public class KilledByCrossbowSerializer extends CriterionSerializer<KilledByCrossbowTrigger.Instance> {

    public KilledByCrossbowSerializer() {
        super(KilledByCrossbowTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED_BY_CROSSBOW);
    }

    @Override
    public void write(KilledByCrossbowTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeVarInt(instance.entities.length);
        for (EntityPredicate.AndPredicate predicate : instance.entities) {
            PacketUtil.writeEntityPredicate(LootUtil.asEntity(predicate), buffer);
        }
        PacketUtil.writeIntRange(instance.bounds, buffer);
    }

    @Override
    public KilledByCrossbowTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate[] entities = new EntityPredicate.AndPredicate[buffer.readVarInt()];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        }
        MinMaxBounds.IntBound bounds = PacketUtil.readIntRange(buffer);
        return new KilledByCrossbowTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, entities, bounds);
    }
}
