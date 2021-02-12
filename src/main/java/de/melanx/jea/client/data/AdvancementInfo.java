package de.melanx.jea.client.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class AdvancementInfo {
    
    public final ResourceLocation id;
    private final DisplayInfo display;
    private final IFormattableTextComponent formattedDisplayName;

    private AdvancementInfo(Advancement advancement) {
        this.id = advancement.getId();
        this.display = Objects.requireNonNull(advancement.getDisplay());
        this.formattedDisplayName = this.display.getTitle().deepCopy().mergeStyle(this.display.getFrame().getFormat());
    }
    
    private AdvancementInfo(PacketBuffer buffer) {
        this.id = buffer.readResourceLocation();
        this.display = DisplayInfo.read(buffer);
        this.formattedDisplayName = this.display.getTitle().deepCopy().mergeStyle(this.display.getFrame().getFormat());
    }
    
    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.id);
        this.display.write(buffer);
    }
    
    public static Optional<AdvancementInfo> create(Advancement advancement) {
        if (advancement.getDisplay() != null && !advancement.getDisplay().isHidden()) {
            return Optional.of(new AdvancementInfo(advancement));
        } else {
            return Optional.empty();
        }
    }
    
    public static Stream<AdvancementInfo> createAsStream(Advancement advancement) {
        return create(advancement).map(Stream::of).orElse(Stream.empty());
    }
    
    public static AdvancementInfo read(PacketBuffer buffer) {
        return new AdvancementInfo(buffer);
    }

    public DisplayInfo getDisplay() {
        return this.display;
    }

    public IFormattableTextComponent getFormattedDisplayName() {
        return this.formattedDisplayName;
    }

    @Override
    public String toString() {
        return "AdvancementInfo {" +
                "id=" + this.id +
                ", display=" + this.display.serialize() +
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
