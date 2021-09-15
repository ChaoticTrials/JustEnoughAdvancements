package de.melanx.jea.api;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A serializer to serialise objects of a specific type of ICriterionInstance.
 * This should be registered with the same id as the criterion instance to work.
 * If the criterion instance uses dynamic ids such as {@code KilledTrigger} you
 * should override {@code checkValidity}
 */
public abstract class CriterionSerializer<T extends CriterionTriggerInstance> extends ForgeRegistryEntry.UncheckedRegistryEntry<CriterionSerializer<?>> {

    public final Class<T> criterionClass;

    protected CriterionSerializer(Class<T> criterionClass) {
        this.criterionClass = criterionClass;
    }
    
    public final boolean isValid(@Nullable CriterionTriggerInstance instance) {
        return instance != null && this.criterionClass.isAssignableFrom(instance.getClass()) && this.checkValidity(instance);
    }
    
    protected boolean checkValidity(CriterionTriggerInstance instance) {
        return this.getRegistryName() != null && this.getRegistryName().equals(instance.getCriterion());
    }

    public abstract void write(T instance, FriendlyByteBuf buffer);
    
    public abstract T read(FriendlyByteBuf buffer);
    
    public static Optional<CriterionSerializer<?>> getSerializer(CriterionTriggerInstance instance) {
        return JeaRegistries.CRITERION_SERIALIZER.getValues().stream()
                .filter(serializer -> serializer.isValid(instance))
                .findFirst();
    }
}
