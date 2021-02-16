package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.LootUtil;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.PacketBuffer;

public class FishingRodHookSerializer extends CriterionSerializer<FishingRodHookedTrigger.Instance> {

    public FishingRodHookSerializer() {
        super(FishingRodHookedTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.FISHING_ROD_HOOK);
    }

    @Override
    public void write(FishingRodHookedTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.rod, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public FishingRodHookedTrigger.Instance read(PacketBuffer buffer) {
        ItemPredicate rod = PacketUtil.readItemPredicate(buffer);
        EntityPredicate.AndPredicate bobber = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new FishingRodHookedTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, rod, bobber, item);
    }
}
