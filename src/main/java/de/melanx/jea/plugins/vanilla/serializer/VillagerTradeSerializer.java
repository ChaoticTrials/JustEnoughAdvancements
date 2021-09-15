package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class VillagerTradeSerializer extends CriterionSerializer<TradeTrigger.TriggerInstance> {

    public VillagerTradeSerializer() {
        super(TradeTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.VILLAGER_TRADE);
    }

    @Override
    public void write(TradeTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.villager), buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public TradeTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        EntityPredicate.Composite villager = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, villager, item);
    }
}
