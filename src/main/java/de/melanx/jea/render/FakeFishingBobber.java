package de.melanx.jea.render;

import net.minecraft.world.entity.projectile.FishingHook;

public class FakeFishingBobber extends FishingHook {

    public FakeFishingBobber(FakeClientPlayer player) {
        super(player, player.level, 0, 0);
    }
}
