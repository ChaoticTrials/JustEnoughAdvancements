package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.util.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.VillagerTradeTrigger;
import net.minecraft.network.PacketBuffer;

public class VillagerTradeSerializer extends CriterionSerializer<VillagerTradeTrigger.Instance> {

    public VillagerTradeSerializer() {
        super(VillagerTradeTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.VILLAGER_TRADE);
    }

    @Override
    public void write(VillagerTradeTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.villager), buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public VillagerTradeTrigger.Instance read(PacketBuffer buffer) {
        EntityPredicate.AndPredicate villager = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new VillagerTradeTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, villager, item);
    }
}
