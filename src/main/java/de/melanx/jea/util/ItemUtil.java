package de.melanx.jea.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.LazyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemUtil {

    public static final List<UUID> CREATORS = ImmutableList.of(
            UUID.fromString("29949ea0-7355-4d4e-915f-434564b67891"), // MelanX
            UUID.fromString("3358ddae-3a41-4ba0-bdfa-ee54b6c55cf5")  // noeppi_noeppi
    );

    private static final LazyValue<List<ItemStack>> STACKS = new LazyValue<>(() -> CREATORS.stream().map(ItemUtil::getHead).collect(Collectors.toList()));

    public static List<ItemStack> creators() {
        return STACKS.getValue();
    }

    private static ItemStack getHead(UUID player) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        CompoundNBT nbt = stack.getOrCreateTag();
        GameProfile profile = fillProfile(player);
        CompoundNBT skullOwner = new CompoundNBT();
        skullOwner.putUniqueId("Id", player);
        nbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), profile));
        stack.setTag(nbt);
        return stack;
    }

    @Nonnull
    private static GameProfile fillProfile(@Nonnull UUID player) {
        GameProfile input = new GameProfile(player, null);
        if (input.isComplete() && input.getProperties().containsKey("textures")) {
            return input;
        } else if (SkullTileEntity.profileCache != null && SkullTileEntity.sessionService != null) {
            GameProfile profile = SkullTileEntity.profileCache.getProfileByUUID(input.getId());
            if (profile == null) {
                return input;
            } else {
                Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);
                if (property == null) {
                    profile = SkullTileEntity.sessionService.fillProfileProperties(profile, true);
                }
                return profile;
            }
        } else {
            return input;
        }
    }
}
