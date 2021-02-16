package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.FilledBucketTrigger;
import net.minecraft.network.PacketBuffer;

public class FillBucketSerializer extends CriterionSerializer<FilledBucketTrigger.Instance> {

    public FillBucketSerializer() {
        super(FilledBucketTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.FILL_BUCKET);
    }

    @Override
    public void write(FilledBucketTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public FilledBucketTrigger.Instance read(PacketBuffer buffer) {
        return new FilledBucketTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readItemPredicate(buffer));
    }
}
