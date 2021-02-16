package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;

public class ChannelingLightningSerializer extends CriterionSerializer<ChanneledLightningTrigger.Instance> {

    public ChannelingLightningSerializer() {
        super(ChanneledLightningTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANNELING_LIGHTNING);
    }

    @Override
    public void write(ChanneledLightningTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeVarInt(instance.victims.length);
        for (EntityPredicate.AndPredicate predicate : instance.victims) {
            PacketUtil.writeEntityPredicate(LootUtil.asEntity(predicate), buffer);
        }
    }

    @Override
    public ChanneledLightningTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate[] victims = new EntityPredicate.AndPredicate[buffer.readVarInt()];
        for (int i = 0; i < victims.length; i++) {
            victims[i] = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        }
        return new ChanneledLightningTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, victims);
    }
}
