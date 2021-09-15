//package de.melanx.jea.plugins.botania.serializer;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.network.PacketUtil;
//import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.network.FriendlyByteBuf;
//import vazkii.botania.common.advancements.CorporeaRequestTrigger;
//
//public class CorporeaRequestSerializer extends CriterionSerializer<CorporeaRequestTrigger.Instance> {
//
//    public CorporeaRequestSerializer() {
//        super(CorporeaRequestTrigger.Instance.class);
//        this.setRegistryName(BotaniaCriteriaIds.CORPOREA_REQUEST);
//    }
//
//    @Override
//    public void write(CorporeaRequestTrigger.Instance instance, FriendlyByteBuf buffer) {
//        PacketUtil.writeIntRange(instance.getCount(), buffer);
//        PacketUtil.writeLocationPredicate(instance.getIndexPos(), buffer);
//    }
//
//    @Override
//    public CorporeaRequestTrigger.Instance read(FriendlyByteBuf buffer) {
//        return new CorporeaRequestTrigger.Instance(EntityPredicate.Composite.ANY,
//                PacketUtil.readIntRange(buffer),
//                PacketUtil.readLocationPredicate(buffer)
//        );
//    }
//}
