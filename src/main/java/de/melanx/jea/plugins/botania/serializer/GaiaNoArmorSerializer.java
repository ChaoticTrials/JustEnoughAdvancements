package de.melanx.jea.plugins.botania.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;

public class GaiaNoArmorSerializer extends CriterionSerializer<DopplegangerNoArmorTrigger.Instance> {

    public GaiaNoArmorSerializer() {
        super(DopplegangerNoArmorTrigger.Instance.class);
        this.setRegistryName(BotaniaCriteriaIds.GAIA_NO_ARMOR);
    }

    @Override
    public void write(DopplegangerNoArmorTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeEntityPredicate(instance.getGuardian(), buffer);
        PacketUtil.writeDamageSourcePredicate(instance.getKillingBlow(), buffer);
    }

    @Override
    public DopplegangerNoArmorTrigger.Instance read(PacketBuffer buffer) {
        return new DopplegangerNoArmorTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND,
                PacketUtil.readEntityPredicate(buffer),
                PacketUtil.readDamageSourcePredicate(buffer)
        );
    }
}