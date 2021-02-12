package de.melanx.jea;

import de.melanx.jea.client.data.AdvancementInfo;
import de.melanx.jea.client.data.ClientAdvancements;
import de.melanx.jea.ingredient.AdvancementCategory;
import de.melanx.jea.ingredient.AdvancementIngredientHelper;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import mezz.jei.Internal;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

@JeiPlugin
public class JEAPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "jeiplugin");
    public static final IIngredientType<AdvancementInfo> ADVANCEMENT_TYPE = () -> AdvancementInfo.class;

    private static IJeiRuntime runtime;

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registration) {
        registration.register(ADVANCEMENT_TYPE, ClientAdvancements.getAdvancements(), new AdvancementIngredientHelper(), new AdvancementIngredientRenderer());
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new AdvancementCategory(registration.getJeiHelpers().getGuiHelper(), Internal.getTextures())
        );
    }

    @Override
    public void registerAdvanced(@Nonnull IAdvancedRegistration registration) {

    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime runtime) {
        JEAPlugin.runtime = runtime;
    }

    public static void runtime(Consumer<IJeiRuntime> action) {
        if (runtime != null) {
            action.accept(runtime);
        } else {
            throw new IllegalStateException("JEI runtime is not yet available.");
        }
    }

    public static <T> T runtime(Function<IJeiRuntime, T> action) {
        if (runtime != null) {
            return action.apply(runtime);
        } else {
            throw new IllegalStateException("JEI runtime is not yet available.");
        }
    }
}
