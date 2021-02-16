package de.melanx.jea.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FakeClientPlayer extends AbstractClientPlayerEntity {

    public FakeClientPlayer(ClientWorld world) {
        super(world, new GameProfile(null, "Steve"));
    }

    @Nonnull
    @Override
    public ResourceLocation getLocationSkin() {
        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }
}
