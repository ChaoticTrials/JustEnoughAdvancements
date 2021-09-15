package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BrewPotionSerializer extends CriterionSerializer<BrewedPotionTrigger.TriggerInstance> {
    
    public BrewPotionSerializer() {
        super(BrewedPotionTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.BREW_POTION);
    }

    @Override
    public void write(BrewedPotionTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        buffer.writeBoolean(instance.potion != null);
        if (instance.potion != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.potion.getRegistryName()));
        }
    }

    @Override
    public BrewedPotionTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        Potion potion = null;
        if (buffer.readBoolean()) {
            potion = Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(buffer.readResourceLocation()));
        }
        return new BrewedPotionTrigger.TriggerInstance(EntityPredicate.Composite.ANY, potion);
    }
}
