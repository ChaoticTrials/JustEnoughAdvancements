package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class FillBucketSerializer extends CriterionSerializer<FilledBucketTrigger.TriggerInstance> {

    public FillBucketSerializer() {
        super(FilledBucketTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.FILL_BUCKET);
    }

    @Override
    public void write(FilledBucketTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public FilledBucketTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new FilledBucketTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readItemPredicate(buffer));
    }
}
