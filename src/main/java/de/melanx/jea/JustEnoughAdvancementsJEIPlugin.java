package de.melanx.jea;

import de.melanx.jea.api.client.Jea;
import de.melanx.jea.client.ClientAdvancements;
import de.melanx.jea.ingredient.AdvancementIngredientHelper;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import de.melanx.jea.recipe.AdvancementCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

@JeiPlugin
public class JustEnoughAdvancementsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "jeiplugin");
    
    private static IJeiRuntime runtime;
    
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registration) {
        registration.register(Jea.ADVANCEMENT_TYPE, ClientAdvancements.getIAdvancements(), new AdvancementIngredientHelper(), new AdvancementIngredientRenderer());
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new AdvancementCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        registration.addRecipes(ClientAdvancements.collectRecipes(), AdvancementCategory.UID);
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime runtime) {
        JustEnoughAdvancementsJEIPlugin.runtime = runtime;
    }

    public static void runtimeOptional(Consumer<IJeiRuntime> action) {
        if (runtime != null) {
            action.accept(runtime);
        }
    }
    
    public static void runtime(Consumer<IJeiRuntime> action) {
        if (runtime != null) {
            action.accept(runtime);
        } else {
            throw new IllegalStateException("JEI runtime is not yet available.");
        }
    }

    public static <T> T runtimeResult(Function<IJeiRuntime, T> action) {
        if (runtime != null) {
            return action.apply(runtime);
        } else {
            throw new IllegalStateException("JEI runtime is not yet available.");
        }
    }
}
