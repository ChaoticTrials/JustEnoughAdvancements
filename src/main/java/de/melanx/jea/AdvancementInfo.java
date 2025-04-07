package de.melanx.jea;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.config.JeaConfig;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Optional;

public final class AdvancementInfo implements IAdvancementInfo {

    public final ResourceLocation id;
    private final DisplayInfo display;
    private final Optional<ResourceLocation> parent;
    private final Component formattedDisplayName;

    public static final Codec<AdvancementInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AdvancementInfo::getId),
            DisplayInfo.CODEC.fieldOf("display").forGetter(AdvancementInfo::getDisplay),
            ResourceLocation.CODEC.optionalFieldOf("parent").forGetter(AdvancementInfo::getParent)
    ).apply(instance, AdvancementInfo::new));

    private AdvancementInfo(ResourceLocation id, DisplayInfo display, Optional<ResourceLocation> parent) {
        this.id = id;
        this.display = display;
        this.parent = parent;
        this.formattedDisplayName = this.display.getTitle().copy().withStyle(this.display.getType().getChatColor());
    }

    private AdvancementInfo(AdvancementHolder advancement) {
        this(
                advancement.id(),
                Objects.requireNonNull(advancement.value().display().orElse(null)),
                advancement.value().parent()
        );
    }

    private AdvancementInfo(RegistryFriendlyByteBuf buffer) {
        this(
                buffer.readResourceLocation(),
                DisplayInfo.fromNetwork(buffer),
                buffer.readOptional(FriendlyByteBuf::readResourceLocation)
        );
    }

    private AdvancementInfo(IAdvancementInfo wrap) {
        this(wrap.getId(), wrap.getDisplay(), wrap.getParent());
    }

    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        this.display.serializeToNetwork(buffer);
        buffer.writeOptional(this.parent, FriendlyByteBuf::writeResourceLocation);
    }

    public static Optional<AdvancementInfo> create(AdvancementHolder advancement) {
        if (advancement.value().display().isPresent() && (JeaConfig.hiddenAdvancements || !advancement.value().display().get().isHidden())) {
            return Optional.of(new AdvancementInfo(advancement));
        } else {
            return Optional.empty();
        }
    }

    public static AdvancementInfo read(RegistryFriendlyByteBuf buffer) {
        return new AdvancementInfo(buffer);
    }

    public static AdvancementInfo get(IAdvancementInfo info) {
        if (info instanceof AdvancementInfo impl) {
            return impl;
        }

        JustEnoughAdvancements.LOGGER.warn("IAdvancementInfo found that is not an instance of AdvancementInfo. This should nt happen. Another mod may have created their own implementation of IAdvancementInfo which is not supported. Class is {}", info.getClass());
        return new AdvancementInfo(info);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public DisplayInfo getDisplay() {
        return this.display;
    }

    @Override
    public Optional<ResourceLocation> getParent() {
        return this.parent;
    }

    @Override
    public Component getFormattedDisplayName() {
        return this.formattedDisplayName;
    }

    @Override
    public String toString() {
        return "AdvancementInfo {" +
                "id=" + this.id +
                ", display=" + DisplayInfo.CODEC.encodeStart(JsonOps.INSTANCE, this.display).getOrThrow() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        AdvancementInfo that = (AdvancementInfo) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
