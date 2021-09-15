package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.ChanneledLightningTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class ChannelingLightningSerializer extends CriterionSerializer<ChanneledLightningTrigger.TriggerInstance> {

    public ChannelingLightningSerializer() {
        super(ChanneledLightningTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.CHANNELING_LIGHTNING);
    }

    @Override
    public void write(ChanneledLightningTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        buffer.writeVarInt(instance.victims.length);
        for (EntityPredicate.Composite predicate : instance.victims) {
            PacketUtil.writeEntityPredicate(LootUtil.asEntity(predicate), buffer);
        }
    }

    @Override
    public ChanneledLightningTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite[] victims = new EntityPredicate.Composite[buffer.readVarInt()];
        for (int i = 0; i < victims.length; i++) {
            victims[i] = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        }
        return new ChanneledLightningTrigger.TriggerInstance(EntityPredicate.Composite.ANY, victims);
    }
}
