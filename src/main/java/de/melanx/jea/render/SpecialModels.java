package de.melanx.jea.render;

import de.melanx.jea.JustEnoughAdvancements;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

import java.util.HashMap;

public class SpecialModels {
    
    public static final ResourceLocation UNKNOWN_ENTITY = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "special/unknown_entity");
    
    private static final HashMap<ResourceLocation, IBakedModel> models = new HashMap<>();
    
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(
                UNKNOWN_ENTITY
        );
    }

    public static void bakeModels(ModelBakeEvent event) {
        models.put(UNKNOWN_ENTITY, event.getModelRegistry().get(UNKNOWN_ENTITY));
    }
    
    public static IBakedModel getModel(ResourceLocation id) {
        return models.get(id);
    }
}
