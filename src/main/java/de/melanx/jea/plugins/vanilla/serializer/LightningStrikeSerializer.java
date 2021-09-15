package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LightningStrikeTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class LightningStrikeSerializer extends CriterionSerializer<LightningStrikeTrigger.TriggerInstance> {

    public LightningStrikeSerializer() {
        super(LightningStrikeTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.LIGHTNING_STRIKE);
    }

    @Override
    public void write(LightningStrikeTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.lightning), buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.bystander), buffer);
    }

    @Override
    public LightningStrikeTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new LightningStrikeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)), LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
