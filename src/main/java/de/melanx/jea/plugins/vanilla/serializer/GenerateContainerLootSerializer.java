package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class GenerateContainerLootSerializer extends CriterionSerializer<LootTableTrigger.TriggerInstance> {

    public GenerateContainerLootSerializer() {
        super(LootTableTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.GENERATE_CONTAINER_LOOT);
    }

    @Override
    public void write(LootTableTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(instance.lootTable);
    }

    @Override
    public LootTableTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new LootTableTrigger.TriggerInstance(EntityPredicate.Composite.ANY, buffer.readResourceLocation());
    }
}
