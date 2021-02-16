package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.PlayerGeneratesContainerLootTrigger;
import net.minecraft.network.PacketBuffer;

public class GenerateContainerLootSerializer extends CriterionSerializer<PlayerGeneratesContainerLootTrigger.Instance> {

    public GenerateContainerLootSerializer() {
        super(PlayerGeneratesContainerLootTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.GENERATE_CONTAINER_LOOT);
    }

    @Override
    public void write(PlayerGeneratesContainerLootTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeResourceLocation(instance.generatedLoot);
    }

    @Override
    public PlayerGeneratesContainerLootTrigger.Instance read(PacketBuffer buffer) {
        return new PlayerGeneratesContainerLootTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, buffer.readResourceLocation());
    }
}
