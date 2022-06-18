package de.melanx.jea.api.client;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

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
     * Gets the formatted component used as title for the advancement. Use this instead of
     * {@code DisplayInfo#getTitle}
     */
    Component getFormattedDisplayName();

    /**
     * Gets the parent advancement id or null if this advancement has no parent.
     */
    @Nullable
    ResourceLocation getParent();
}
