package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.util.LootUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.FriendlyByteBuf;

public class FishingRodHookSerializer extends CriterionSerializer<FishingRodHookedTrigger.TriggerInstance> {

    public FishingRodHookSerializer() {
        super(FishingRodHookedTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.FISHING_ROD_HOOK);
    }

    @Override
    public void write(FishingRodHookedTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.rod, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public FishingRodHookedTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        ItemPredicate rod = PacketUtil.readItemPredicate(buffer);
        EntityPredicate.Composite bobber = LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer));
        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
        return new FishingRodHookedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, rod, bobber, item);
    }
}
