package de.melanx.jea.api.client;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
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
     * Gets which criteria must be completed to complete the advancement.
     * To compete an advancement, there must be at least one criterion completed from
     * each sub-list.
     */
    List<List<String>> getCompletion();

    /**
     * Gets the formatted component used as title for the advancement. Use this instead of
     * {@code DisplayInfo#getTitle}
     */
    MutableComponent getFormattedDisplayName();

    /**
     * Gets the parent advancement id or null if this advancement has no parent.
     */
    @Nullable
    ResourceLocation getParent();
}
