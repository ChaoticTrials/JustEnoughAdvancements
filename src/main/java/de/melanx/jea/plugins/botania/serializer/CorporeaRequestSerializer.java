package de.melanx.jea.plugins.botania.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.common.advancements.CorporeaRequestTrigger;

public class CorporeaRequestSerializer extends CriterionSerializer<CorporeaRequestTrigger.Instance> {

    public CorporeaRequestSerializer() {
        super(CorporeaRequestTrigger.Instance.class);
        this.setRegistryName(BotaniaCriteriaIds.CORPOREA_REQUEST);
    }

    @Override
    public void write(CorporeaRequestTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeIntRange(instance.getCount(), buffer);
        PacketUtil.writeLocationPredicate(instance.getIndexPos(), buffer);
    }

    @Override
    public CorporeaRequestTrigger.Instance read(PacketBuffer buffer) {
        return new CorporeaRequestTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                PacketUtil.readIntRange(buffer),
                PacketUtil.readLocationPredicate(buffer)
        );
    }
}