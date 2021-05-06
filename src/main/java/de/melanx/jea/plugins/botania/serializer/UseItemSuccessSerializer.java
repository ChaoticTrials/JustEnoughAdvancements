package de.melanx.jea.plugins.botania.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;

public class UseItemSuccessSerializer extends CriterionSerializer<UseItemSuccessTrigger.Instance> {

    public UseItemSuccessSerializer() {
        super(UseItemSuccessTrigger.Instance.class);
        this.setRegistryName(BotaniaCriteriaIds.USE_ITEM_SUCCESS);
    }

    @Override
    public void write(UseItemSuccessTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.getItem(), buffer);
        PacketUtil.writeLocationPredicate(instance.getLocation(), buffer);
    }

    @Override
    public UseItemSuccessTrigger.Instance read(PacketBuffer buffer) {
        return new UseItemSuccessTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                PacketUtil.readItemPredicate(buffer),
                PacketUtil.readLocationPredicate(buffer)
        );
    }
}
