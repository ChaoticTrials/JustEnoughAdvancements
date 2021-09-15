package de.melanx.jea.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class FakeClientPlayer extends AbstractClientPlayer {

    public FakeClientPlayer(ClientLevel level) {
        super(level, new GameProfile(null, "Steve"));
    }

    @Nonnull
    @Override
    public ResourceLocation getSkinTextureLocation() {
        return DefaultPlayerSkin.getDefaultSkin();
    }
}
