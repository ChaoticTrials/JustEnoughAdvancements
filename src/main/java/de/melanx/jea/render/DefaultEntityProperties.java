package de.melanx.jea.render;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class DefaultEntityProperties {
    
    public static final DefaultEntityProperties DEFAULT = new DefaultEntityProperties(null, false, false, false, ItemStack.EMPTY, 0, 0, 0, 0, 0);
    public static final DefaultEntityProperties BABY = new DefaultEntityProperties(null, false, false, true, ItemStack.EMPTY, 0, 0, 0, 0, 0);
    public static final DefaultEntityProperties ZOMBIE_VILLAGER = new DefaultEntityProperties(EntityType.ZOMBIE_VILLAGER, false, false, false, ItemStack.EMPTY, 0, 0, 0, 0, 0);
    public static final DefaultEntityProperties VILLAGER = new DefaultEntityProperties(EntityType.VILLAGER, false, false, false, ItemStack.EMPTY, 0, 0, 0, 0, 0);
    
    @Nullable
    public final EntityType<?> type;
    public final boolean typeDisplayOnly;
    public final boolean fire;
    public final boolean baby;
    private final ItemStack held;
    public final float swing;
    public final float limbSwing;
    public final int useTick;
    public final int hurtTime;
    public final int deathTime;

    public DefaultEntityProperties(@Nullable EntityType<?> type, boolean typeDisplayOnly, boolean fire, boolean baby, ItemStack held, float swing, float limbSwing, int useTick, int hurtTime, int deathTime) {
        this.type = type;
        this.typeDisplayOnly = typeDisplayOnly;
        this.fire = fire;
        this.baby = baby;
        this.held = held;
        this.swing = swing;
        this.limbSwing = limbSwing;
        this.useTick = useTick;
        this.hurtTime = hurtTime;
        this.deathTime = deathTime;
    }

    public ItemStack getHeld() {
        return this.held;
    }
}
