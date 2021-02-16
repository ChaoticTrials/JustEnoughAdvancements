package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.BrewedPotionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BrewPotionSerializer extends CriterionSerializer<BrewedPotionTrigger.Instance> {
    
    public BrewPotionSerializer() {
        super(BrewedPotionTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.BREW_POTION);
    }

    @Override
    public void write(BrewedPotionTrigger.Instance instance, PacketBuffer buffer) {
        buffer.writeBoolean(instance.potion != null);
        if (instance.potion != null) {
            buffer.writeResourceLocation(Objects.requireNonNull(instance.potion.getRegistryName()));
        }
    }

    @Override
    public BrewedPotionTrigger.Instance read(PacketBuffer buffer) {
        Potion potion = null;
        if (buffer.readBoolean()) {
            potion = Objects.requireNonNull(ForgeRegistries.POTION_TYPES.getValue(buffer.readResourceLocation()));
        }
        return new BrewedPotionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, potion);
    }
}
