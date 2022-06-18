package de.melanx.jea;

import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.config.JeaConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public final class AdvancementInfo implements IAdvancementInfo {
    
    public final ResourceLocation id;
    private final DisplayInfo display;
    @Nullable
    private final ResourceLocation parent;
    private final Component formattedDisplayName;

    private AdvancementInfo(Advancement advancement) {
        this.id = advancement.getId();
        this.display = Objects.requireNonNull(advancement.getDisplay());
        this.parent = advancement.getParent() == null ? null : advancement.getParent().getId();
        this.formattedDisplayName = this.display.getTitle().copy().withStyle(this.display.getFrame().getChatColor());
    }
    
    private AdvancementInfo(FriendlyByteBuf buffer) {
        this.id = buffer.readResourceLocation();
        this.display = DisplayInfo.fromNetwork(buffer);
        this.parent = buffer.readBoolean() ? buffer.readResourceLocation() : null;
        this.formattedDisplayName = this.display.getTitle().copy().withStyle(this.display.getFrame().getChatColor());
    }
    
    private AdvancementInfo(IAdvancementInfo wrap) {
        this.id = wrap.getId();
        this.display = wrap.getDisplay();
        this.parent = wrap.getParent();
        this.formattedDisplayName = this.display.getTitle().copy().withStyle(this.display.getFrame().getChatColor());
    }
    
    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        this.display.serializeToNetwork(buffer);
        buffer.writeBoolean(this.parent != null);
        if (this.parent != null) {
            buffer.writeResourceLocation(this.parent);
        }
    }
    
    public static Optional<AdvancementInfo> create(Advancement advancement) {
        if (advancement.getDisplay() != null && (JeaConfig.hiddenAdvancements || !advancement.getDisplay().isHidden())) {
            return Optional.of(new AdvancementInfo(advancement));
        } else {
            return Optional.empty();
        }
    }
    
    public static AdvancementInfo read(FriendlyByteBuf buffer) {
        return new AdvancementInfo(buffer);
    }

    public static AdvancementInfo get(IAdvancementInfo info) {
        if (info instanceof AdvancementInfo impl) {
            return impl;
        } else {
            JustEnoughAdvancements.logger.warn("IAdvancementInfo found that is not an instance of AdvancementInfo. This should nt happen. Another mod may have created their own implementation of IAdvancementInfo which is not supported. Class is " + info.getClass());
            return new AdvancementInfo(info);
        }
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public DisplayInfo getDisplay() {
        return this.display;
    }

    @Nullable
    @Override
    public ResourceLocation getParent() {
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
                ", display=" + this.display.serializeToJson() +
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
