package de.melanx.jea;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.melanx.jea.api.client.IAdvancementInfo;
import de.melanx.jea.config.JeaConfig;
import de.melanx.jea.network.PacketUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public final class AdvancementInfo implements IAdvancementInfo {
    
    public final ResourceLocation id;
    private final DisplayInfo display;
    private final Map<String, Criterion> criteria;
    private final List<List<String>> completion;
    @Nullable
    private final ResourceLocation parent;
    private final IFormattableTextComponent formattedDisplayName;
    private final Map<String, ResourceLocation> criteriaSerializerIds;

    private AdvancementInfo(Advancement advancement) {
        this.id = advancement.getId();
        this.display = Objects.requireNonNull(advancement.getDisplay());
        //noinspection ConstantConditions
        this.criteria = advancement.getCriteria() != null ? ImmutableMap.copyOf(advancement.getCriteria()) : ImmutableMap.of();
        //noinspection UnstableApiUsage
        this.completion = Arrays.stream(advancement.getRequirements()).map(array ->
                Arrays.stream(array).filter(this.criteria::containsKey).collect(ImmutableList.toImmutableList())
        ).collect(ImmutableList.toImmutableList());
        this.parent = advancement.getParent() == null ? null : advancement.getParent().getId();
        this.formattedDisplayName = this.display.getTitle().deepCopy().mergeStyle(this.display.getFrame().getFormat());
        this.criteriaSerializerIds = PacketUtil.getCriteriaSerializers(this.criteria);
    }
    
    private AdvancementInfo(PacketBuffer buffer) {
        this.id = buffer.readResourceLocation();
        this.display = DisplayInfo.read(buffer);
        Pair<Map<String, Criterion>, Map<String, ResourceLocation>> criteriaData = PacketUtil.readCriteria(buffer);
        this.criteria = criteriaData.getLeft();
        this.criteriaSerializerIds = criteriaData.getRight();
        this.completion = PacketUtil.readCompletion(buffer);
        this.parent = buffer.readBoolean() ? buffer.readResourceLocation() : null;
        this.formattedDisplayName = this.display.getTitle().deepCopy().mergeStyle(this.display.getFrame().getFormat());
    }
    
    private AdvancementInfo(IAdvancementInfo wrap) {
        this.id = wrap.getId();
        this.display = wrap.getDisplay();
        this.criteria = ImmutableMap.copyOf(wrap.getCriteria());
        //noinspection UnstableApiUsage
        this.completion = wrap.getCompletion().stream().map(ImmutableList::copyOf).collect(ImmutableList.toImmutableList());
        this.parent = wrap.getParent();
        this.formattedDisplayName = this.display.getTitle().deepCopy().mergeStyle(this.display.getFrame().getFormat());
        this.criteriaSerializerIds = PacketUtil.getCriteriaSerializers(this.criteria);
    }
    
    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.id);
        this.display.write(buffer);
        PacketUtil.writeCriteria(buffer, this.criteria, this.criteriaSerializerIds);
        PacketUtil.writeCompletion(buffer, this.completion);
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
    
    public static Stream<AdvancementInfo> createAsStream(Advancement advancement) {
        return create(advancement).map(Stream::of).orElse(Stream.empty());
    }
    
    public static AdvancementInfo read(PacketBuffer buffer) {
        return new AdvancementInfo(buffer);
    }

    public static AdvancementInfo get(IAdvancementInfo info) {
        if (info instanceof AdvancementInfo) {
            return (AdvancementInfo) info;
        } else {
            JustEnoughAdvancements.logger.warn("IAdvancementInfo found that is not an instance of AdvancementInfo. This should nt happen. Another mod may have created their own implementation of IAdvancementInfo which is not supported. CLass is " + info.getClass());
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

    @Override
    public Map<String, Criterion> getCriteria() {
        return this.criteria;
    }

    @Override
    public List<List<String>> getCompletion() {
        return this.completion;
    }

    @Nullable
    @Override
    public ResourceLocation getParent() {
        return this.parent;
    }

    @Override
    public IFormattableTextComponent getFormattedDisplayName() {
        return this.formattedDisplayName;
    }

    public Map<String, ResourceLocation> getCriteriaSerializerIds() {
        return this.criteriaSerializerIds;
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
