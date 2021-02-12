package de.melanx.jea.client.data;

import com.google.common.collect.ImmutableMap;
import de.melanx.jea.JEAPlugin;
import de.melanx.jea.client.JeiUtil;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class ClientAdvancements {
    
    private static ImmutableMap<ResourceLocation, AdvancementInfo> advancements = ImmutableMap.of();
    
    public static void update(Set<AdvancementInfo> info) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            //noinspection UnstableApiUsage
            advancements = info.stream().collect(ImmutableMap.toImmutableMap(x -> x.id, Function.identity()));
            JEAPlugin.runtime(runtime -> {
                IIngredientManager mgr = runtime.getIngredientManager();
                Collection<AdvancementInfo> ingredients = mgr.getAllIngredients(JEAPlugin.ADVANCEMENT_TYPE);
                if (!ingredients.isEmpty()) {
                    mgr.removeIngredientsAtRuntime(JEAPlugin.ADVANCEMENT_TYPE, ingredients);
                }
                if (!advancements.isEmpty()) {
                    mgr.addIngredientsAtRuntime(JEAPlugin.ADVANCEMENT_TYPE, advancements.values());
                }
            });
            JeiUtil.triggerReload();
        }
    }
    
    public static Set<ResourceLocation> getAdvancementIds() {
        return advancements.keySet();
    }
    
    public static Collection<AdvancementInfo> getAdvancements() {
        return advancements.values();
    }
    
    public static AdvancementInfo getInfo(ResourceLocation key) {
        return advancements.get(key);
    }
}
