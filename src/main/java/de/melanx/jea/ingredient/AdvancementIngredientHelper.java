package de.melanx.jea.ingredient;

import com.google.common.collect.Streams;
import de.melanx.jea.api.client.IAdvancementInfo;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancementIngredientHelper implements IIngredientHelper<IAdvancementInfo> {

    @Nullable
    @Override
    public IAdvancementInfo getMatch(@Nonnull Iterable<IAdvancementInfo> ingredients, @Nonnull IAdvancementInfo advancement) {
        //noinspection UnstableApiUsage
        return Streams.stream(ingredients).filter(a -> advancement.getId().equals(a.getId())).findFirst().orElse(null);
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull IAdvancementInfo advancement) {
        return advancement.getDisplay().getTitle().getString();
    }

    @Nonnull
    @Override
    public String getUniqueId(@Nonnull IAdvancementInfo advancement) {
        return advancement.getId().toString();
    }

    @Nonnull
    @Override
    public String getModId(@Nonnull IAdvancementInfo advancement) {
        return advancement.getId().getNamespace();
    }

    @Nonnull
    @Override
    public String getResourceId(@Nonnull IAdvancementInfo advancement) {
        return advancement.getId().getPath();
    }

    @Nonnull
    @Override
    public IAdvancementInfo copyIngredient(@Nonnull IAdvancementInfo advancement) {
        return advancement;
    }

    @Nonnull
    @Override
    public String getErrorInfo(@Nullable IAdvancementInfo advancement) {
        return advancement == null ? "Unknown advancement" : advancement.toString();
    }
}
