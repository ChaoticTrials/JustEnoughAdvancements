//package de.melanx.jea.plugins.botania.serializer;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.network.PacketUtil;
//import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.network.FriendlyByteBuf;
//import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;
//
//public class GaiaNoArmorSerializer extends CriterionSerializer<DopplegangerNoArmorTrigger.Instance> {
//
//    public GaiaNoArmorSerializer() {
//        super(DopplegangerNoArmorTrigger.Instance.class);
//        this.setRegistryName(BotaniaCriteriaIds.GAIA_NO_ARMOR);
//    }
//
//    @Override
//    public void write(DopplegangerNoArmorTrigger.Instance instance, FriendlyByteBuf buffer) {
//        PacketUtil.writeEntityPredicate(instance.getGuardian(), buffer);
//        PacketUtil.writeDamageSourcePredicate(instance.getKillingBlow(), buffer);
//    }
//
//    @Override
//    public DopplegangerNoArmorTrigger.Instance read(FriendlyByteBuf buffer) {
//        return new DopplegangerNoArmorTrigger.Instance(EntityPredicate.Composite.ANY,
//                PacketUtil.readEntityPredicate(buffer),
//                PacketUtil.readDamageSourcePredicate(buffer)
//        );
//    }
//}
