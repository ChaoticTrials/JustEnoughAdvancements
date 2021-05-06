package de.melanx.jea.plugins.botania.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.common.advancements.AlfPortalTrigger;

public class ActivatePortalSerializer extends CriterionSerializer<AlfPortalTrigger.Instance> {

    public ActivatePortalSerializer() {
        super(AlfPortalTrigger.Instance.class);
        this.setRegistryName(BotaniaCriteriaIds.ACTIVATE_PORTAL);
    }

    @Override
    public void write(AlfPortalTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.getWand(), buffer);
        PacketUtil.writeLocationPredicate(instance.getPos(), buffer);
    }

    @Override
    public AlfPortalTrigger.Instance read(PacketBuffer buffer) {
        return new AlfPortalTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                PacketUtil.readItemPredicate(buffer),
                PacketUtil.readLocationPredicate(buffer)
        );
    }
}
