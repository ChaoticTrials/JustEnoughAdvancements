package de.melanx.jea;

import de.melanx.jea.api.client.Jea;
import de.melanx.jea.client.ClientAdvancements;
import de.melanx.jea.ingredient.AdvancementIngredientHelper;
import de.melanx.jea.ingredient.AdvancementIngredientRenderer;
import de.melanx.jea.plugins.vanilla.criteria.ChannelingLightningInfo;
import de.melanx.jea.plugins.vanilla.criteria.ShootCrossbowInfo;
import de.melanx.jea.render.HealthRender;
import de.melanx.jea.recipe.AdvancementCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

@JeiPlugin
public class JustEnoughAdvancementsJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "jeiplugin");
    
    public static final ResourceLocation MISC_TEXTURE = new ResourceLocation(JustEnoughAdvancements.getInstance().modid, "textures/special/misc.png");
    public static final ResourceLocation ICONS_TEXTURE = new ResourceLocation("minecraft", "textures/gui/icons.png");

    private static IJeiRuntime runtime;
    private static IDrawableStatic slot;
    private static IDrawableAnimated lightning;
    private static IDrawableStatic potionBubbles;
    private static IDrawableStatic hungerEmpty;
    private static IDrawableStatic hungerFull;
    private static IDrawableStatic hungerHalf;
    private static IDrawableStatic hungerSaturation;
    private static IDrawableStatic effectSlot;
    private static IDrawableStatic rightArrow;
    private static IDrawableStatic leftArrow;
    private static IDrawableStatic xpOrb;
    private static IDrawableStatic shotArrow;
    
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
        
        HealthRender.HeartEffect.init(registration.getJeiHelpers().getGuiHelper());
        slot = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();
        lightning = registration.getJeiHelpers().getGuiHelper().drawableBuilder(ChannelingLightningInfo.LIGHTNING_TEXTURE, 0, 0, 84, 84).setTextureSize(128, 128).buildAnimated(8, IDrawableAnimated.StartDirection.TOP, false);
        potionBubbles = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 0, 0, 12, 29);
        hungerEmpty = registration.getJeiHelpers().getGuiHelper().createDrawable(ICONS_TEXTURE, 16, 27, 9, 9);
        hungerFull = registration.getJeiHelpers().getGuiHelper().createDrawable(ICONS_TEXTURE, 52, 27, 9, 9);
        hungerHalf = registration.getJeiHelpers().getGuiHelper().createDrawable(ICONS_TEXTURE, 61, 27, 9, 9);
        hungerSaturation = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 12, 0, 9, 9);
        effectSlot = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 12, 9, 20, 20);
        rightArrow = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 0, 29, 32, 16);
        leftArrow = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 0, 45, 32, 16);
        xpOrb = registration.getJeiHelpers().getGuiHelper().createDrawable(MISC_TEXTURE, 21, 0, 9, 9);
        shotArrow = registration.getJeiHelpers().getGuiHelper().drawableBuilder(ShootCrossbowInfo.ARROW_TEXTURE, 0, 0, 16, 5).setTextureSize(32, 32).build();
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

    public static IDrawableStatic getSlot() {
        return slot;
    }

    public static IDrawableAnimated getLightning() {
        return lightning;
    }

    public static IDrawableStatic getPotionBubbles() {
        return potionBubbles;
    }

    public static IDrawableStatic getHungerEmpty() {
        return hungerEmpty;
    }

    public static IDrawableStatic getHungerFull() {
        return hungerFull;
    }
    
    public static IDrawableStatic getHungerHalf() {
        return hungerHalf;
    }

    public static IDrawableStatic getHungerSaturation() {
        return hungerSaturation;
    }

    public static IDrawableStatic getEffectSlot() {
        return effectSlot;
    }

    public static IDrawableStatic getArrow(boolean left) {
        return left ? leftArrow : rightArrow;
    }

    public static IDrawableStatic getXpOrb() {
        return xpOrb;
    }

    public static IDrawableStatic getShotArrow() {
        return shotArrow;
    }
}
