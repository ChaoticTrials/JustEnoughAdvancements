package de.melanx.jea.render;

import net.minecraft.entity.projectile.FishingBobberEntity;

public class FakeFishingBobber extends FishingBobberEntity {

    public FakeFishingBobber(FakeClientPlayer player) {
        super(player, player.world, 0, 0);
    }
}
