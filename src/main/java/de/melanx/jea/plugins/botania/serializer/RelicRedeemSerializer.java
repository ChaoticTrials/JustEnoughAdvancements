package de.melanx.jea.plugins.botania.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.common.advancements.RelicBindTrigger;

public class RelicRedeemSerializer extends CriterionSerializer<RelicBindTrigger.Instance> {
    
    public RelicRedeemSerializer() {
        super(RelicBindTrigger.Instance.class);
        this.setRegistryName(BotaniaCriteriaIds.RELIC_REDEEM);
    }

    @Override
    public void write(RelicBindTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.getPredicate(), buffer);
    }

    @Override
    public RelicBindTrigger.Instance read(PacketBuffer buffer) {
        return new RelicBindTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                PacketUtil.readItemPredicate(buffer)
        );
    }
}
