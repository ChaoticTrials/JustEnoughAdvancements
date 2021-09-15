package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledByCrossbowTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;

public class KilledByCrossbowSerializer extends CriterionSerializer<KilledByCrossbowTrigger.TriggerInstance> {

    public KilledByCrossbowSerializer() {
        super(KilledByCrossbowTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.KILLED_BY_CROSSBOW);
    }

    @Override
    public void write(KilledByCrossbowTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        buffer.writeVarInt(instance.victims.length);
        for (EntityPredicate.Composite predicate : instance.victims) {
            PacketUtil.writeEntityPredicate(LootUtil.asEntity(predicate), buffer);
        }
        PacketUtil.writeIntRange(instance.uniqueEntityTypes, buffer);
    }

    @Override
    public KilledByCrossbowTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite[] entities = new EntityPredicate.Composite[buffer.readVarInt()];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        }
        MinMaxBounds.Ints bounds = PacketUtil.readIntRange(buffer);
        return new KilledByCrossbowTrigger.TriggerInstance(EntityPredicate.Composite.ANY, entities, bounds);
    }
}
