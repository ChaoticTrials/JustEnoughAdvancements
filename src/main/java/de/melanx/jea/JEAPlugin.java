package de.melanx.jea;

import de.melanx.jea.ingredient.AdvancementInfo;
import de.melanx.jea.ingredient.AdvancementIngredientHelper;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import de.melanx.jea.util.ClientAdvancementInfo;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

@JeiPlugin
public class JEAPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "jeiplugin");
    public static final IIngredientType<AdvancementInfo> TYPE = () -> AdvancementInfo.class;

    private static IJeiRuntime runtime;

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registration) {
        Collection<AdvancementInfo> advancements = ClientAdvancementInfo.getAdvancements();
        registration.register(TYPE, advancements, new AdvancementIngredientHelper(), new AdvancementIngredientRenderer());
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
