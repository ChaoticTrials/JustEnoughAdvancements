package de.melanx.jea.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.melanx.jea.AdvancementInfo;
import de.melanx.jea.JustEnoughAdvancements;
import de.melanx.jea.JustEnoughAdvancementsJEIPlugin;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.internal.JeiRestarter;
import de.melanx.jea.recipe.AdvancementRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.*;
import java.util.function.Function;

public class ClientAdvancements {
    
    private static ImmutableMap<ResourceLocation, AdvancementInfo> advancements = ImmutableMap.of();
    
    public static void update(Set<AdvancementInfo> info) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            advancements = info.stream().collect(ImmutableMap.toImmutableMap(x -> x.id, Function.identity()));
            JustEnoughAdvancementsJEIPlugin.runtimeOptional(runtime -> updateAdvancementIngredientsJEI(runtime, 3));
            JeiRestarter.restart();
        }
    }
    
    private static void updateAdvancementIngredientsJEI(IJeiRuntime runtime, int tries) {
        try {
            IIngredientManager imgr = runtime.getIngredientManager();
            Collection<IAdvancementInfo> ingredients = ImmutableList.copyOf(imgr.getAllIngredients(Jea.ADVANCEMENT_TYPE));
            if (!ingredients.isEmpty()) {
                imgr.removeIngredientsAtRuntime(Jea.ADVANCEMENT_TYPE, ingredients);
            }
            if (!advancements.isEmpty()) {
                imgr.addIngredientsAtRuntime(Jea.ADVANCEMENT_TYPE, getIAdvancements());
            }
        } catch (ConcurrentModificationException e) {
            if (tries > 0) {
                JustEnoughAdvancements.logger.warn("Failed to update advancement ingredients for JEI. Trying again.");
                updateAdvancementIngredientsJEI(runtime, tries - 1);
            } else {
                JustEnoughAdvancements.logger.error("Failed to update advancement ingredients for JEI. Ignoring this for now. Advancements might be out of sync.");
            }
        }
    }
    
    public static Set<ResourceLocation> getAdvancementIds() {
        return advancements.keySet();
    }
    
    public static Collection<AdvancementInfo> getAdvancements() {
        return advancements.values();
    }
    
    public static Collection<IAdvancementInfo> getIAdvancements() {
        //noinspection unchecked
        return (Collection<IAdvancementInfo>) (Collection<?>) advancements.values();
    }
    
    public static AdvancementInfo getInfo(ResourceLocation key) {
        return advancements.get(key);
    }

    public static List<AdvancementRecipe> collectRecipes() {
        List<AdvancementRecipe> recipes = new ArrayList<>();
        for (AdvancementInfo info : ClientAdvancements.getAdvancements()) {
            if (info.getDisplay() != null) {
                recipes.add(new AdvancementRecipe(info));
            }
        }
        JustEnoughAdvancements.logger.info("Collected " + recipes.size() + " advancement criterion recipes.");
        return ImmutableList.copyOf(recipes);
    }
}
