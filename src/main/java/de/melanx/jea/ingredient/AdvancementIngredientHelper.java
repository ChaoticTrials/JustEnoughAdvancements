package de.melanx.jea.ingredient;

import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.api.client.Jea;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class AdvancementIngredientHelper implements IIngredientHelper<IAdvancementInfo> {

    @Nonnull
    @Override
    public IIngredientType<IAdvancementInfo> getIngredientType() {
        return Jea.ADVANCEMENT_TYPE;
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull IAdvancementInfo info) {
        return info.getDisplay().getTitle().getString();
    }

    @Nonnull
    @Override
    public String getUniqueId(@Nonnull IAdvancementInfo info, @Nonnull UidContext ctx) {
        return this.getResourceLocation(info).toString();
    }

    @Nonnull
    @Override
    public ResourceLocation getResourceLocation(@Nonnull IAdvancementInfo info) {
        return info.getId();
    }

    @Nonnull
    @Override
    public IAdvancementInfo copyIngredient(@Nonnull IAdvancementInfo info) {
        return info;
    }

    @Nonnull
    @Override
    public String getErrorInfo(@Nullable IAdvancementInfo info) {
        return info == null ? "Unknown advancement" : info.toString();
    }
}
