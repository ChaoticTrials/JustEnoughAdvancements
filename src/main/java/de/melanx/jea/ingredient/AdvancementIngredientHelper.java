package de.melanx.jea.ingredient;

import com.google.common.collect.Streams;
import mezz.jei.api.ingredients.IIngredientHelper;
import net.minecraft.advancements.Advancement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancementIngredientHelper implements IIngredientHelper<AdvancementInfo> {

    @Nullable
    @Override
    public AdvancementInfo getMatch(@Nonnull Iterable<AdvancementInfo> ingredients, @Nonnull AdvancementInfo advancement) {
        //noinspection UnstableApiUsage
        return Streams.stream(ingredients).filter(a -> advancement.id.equals(a.id)).findFirst().orElse(null);
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull AdvancementInfo advancement) {
        return advancement.translation.getString();
    }

    @Nonnull
    @Override
    public String getUniqueId(@Nonnull AdvancementInfo advancement) {
        return advancement.id.toString();
    }

    @Nonnull
    @Override
    public String getModId(@Nonnull AdvancementInfo advancement) {
        return advancement.id.getNamespace();
    }

    @Nonnull
    @Override
    public String getResourceId(@Nonnull AdvancementInfo advancement) {
        return advancement.id.toString();
    }

    @Nonnull
    @Override
    public AdvancementInfo copyIngredient(@Nonnull AdvancementInfo advancement) {
        return advancement;
    }

    @Nonnull
    @Override
    public String getErrorInfo(@Nullable AdvancementInfo advancement) {
        return "Ja lol ey, da gibt's n Fehler."; // TODO skateIEH
    }
}