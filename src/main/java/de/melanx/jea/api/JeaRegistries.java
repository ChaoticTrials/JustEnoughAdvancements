package de.melanx.jea.api;

import de.melanx.jea.JustEnoughAdvancements;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Contains the registries used by the JEA mod. See {@link de.melanx.jea.api.client.Jea} for more info.
 */
public class JeaRegistries {

    /**
     * The registry for {@link CriterionSerializer CriterionSerializers}.
     */
    @SuppressWarnings("unchecked")
    public static final IForgeRegistry<CriterionSerializer<?>> CRITERION_SERIALIZER = new RegistryBuilder<CriterionSerializer<?>>()
            .setName(new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "criteria_serializers"))
            .setType((Class<CriterionSerializer<?>>) (Class<?>) CriterionSerializer.class)
            .disableOverrides()
            .create();
    
    public static void initRegistries(RegistryEvent.NewRegistry event) {
        //
    }
}
