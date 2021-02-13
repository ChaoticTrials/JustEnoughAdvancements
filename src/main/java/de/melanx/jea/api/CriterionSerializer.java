package de.melanx.jea.api;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A serializer to serialise objects of a specific type of ICriterionInstance.
 * This should be registered with the same id as the criterion instance to work.
 * If the criterion instance uses dynamic ids such as {@code KilledTrigger} you
 * should override {@code checkValidity}
 */
public abstract class CriterionSerializer<T extends ICriterionInstance> extends ForgeRegistryEntry<CriterionSerializer<?>> {

    public final Class<T> criterionClass;

    protected CriterionSerializer(Class<T> criterionClass) {
        this.criterionClass = criterionClass;
    }
    
    public final boolean isValid(@Nullable ICriterionInstance instance) {
        return instance != null && this.criterionClass.isAssignableFrom(instance.getClass()) && this.checkValidity(instance);
    }
    
    protected boolean checkValidity(ICriterionInstance instance) {
        return this.getRegistryName() != null && this.getRegistryName().equals(instance.getId());
    }

    public abstract void write(T instance, PacketBuffer buffer);
    
    public abstract T read(PacketBuffer buffer);
    
    public static Optional<CriterionSerializer<?>> getSerializer(ICriterionInstance instance) {
        return JeaRegistries.CRITERION_SERIALIZER.getValues().stream()
                .filter(serializer -> serializer.isValid(instance))
                .findFirst();
    }
    
    public static Stream<? extends CriterionSerializer<?>> getSerializerAsStream(ICriterionInstance instance) {
        return getSerializer(instance).map(Stream::of).orElse(Stream.empty());
    }
}
