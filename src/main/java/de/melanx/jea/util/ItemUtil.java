package de.melanx.jea.util;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.noeppi_noeppi.libx.util.LazyValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemUtil {

    public static final List<UUID> CREATORS = List.of(
            UUID.fromString("29949ea0-7355-4d4e-915f-434564b67891"), // MelanX
            UUID.fromString("3358ddae-3a41-4ba0-bdfa-ee54b6c55cf5")  // noeppi_noeppi
    );

    private static final LazyValue<List<ItemStack>> STACKS = new LazyValue<>(() -> CREATORS.stream().map(ItemUtil::getHead).collect(Collectors.toList()));

    public static List<ItemStack> creators() {
        return STACKS.get();
    }

    private static ItemStack getHead(UUID player) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag nbt = stack.getOrCreateTag();
        GameProfile profile = fillProfile(player);
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putUUID("Id", player);
        nbt.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));
        stack.setTag(nbt);
        return stack;
    }

    @Nonnull
    private static GameProfile fillProfile(@Nonnull UUID player) {
        GameProfile input = new GameProfile(player, null);
        if (input.isComplete() && input.getProperties().containsKey("textures")) {
            return input;
        } else if (SkullBlockEntity.profileCache != null && SkullBlockEntity.sessionService != null) {
            GameProfile profile = SkullBlockEntity.profileCache.get(input.getId()).orElse(null);
            if (profile == null) {
                return input;
            } else {
                Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);
                if (property == null) {
                    profile = SkullBlockEntity.sessionService.fillProfileProperties(profile, true);
                }
                return profile;
            }
        } else {
            return input;
        }
    }
}
