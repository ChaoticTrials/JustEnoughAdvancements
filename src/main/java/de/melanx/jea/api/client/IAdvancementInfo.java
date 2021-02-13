package de.melanx.jea.api.client;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * An interface describing an advancement in the client.
 * <b>DO NOT CREATE YOUR OWN SUBCLASS OF THIS!</b>
 */
public interface IAdvancementInfo {

    /**
     * Gets the id of the advancement.
     */
    ResourceLocation getId();
    
    /**
     * Gets the display info of the advancement.
     */
    DisplayInfo getDisplay();

    /**
     * Gets the criteria for the advancement.
     */
    Map<String, Criterion> getCriteria();

    /**
     * Gets the formatted component used as title for the advancement. Use this instead of
     * {@code DisplayInfo#getTitle}
     */
    IFormattableTextComponent getFormattedDisplayName();

    /**
     * Gets the parent advancement id or null if this advancement has no parent.
     */
    @Nullable
    ResourceLocation getParent();
}
