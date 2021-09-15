//package de.melanx.jea.plugins.botania.serializer;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.network.PacketUtil;
//import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.network.FriendlyByteBuf;
//import vazkii.botania.common.advancements.LokiPlaceTrigger;
//
//public class LokiPlaceSerializer extends CriterionSerializer<LokiPlaceTrigger.Instance> {
//
//    public LokiPlaceSerializer() {
//        super(LokiPlaceTrigger.Instance.class);
//        this.setRegistryName(BotaniaCriteriaIds.LOKI_PLACE);
//    }
//
//    @Override
//    public void write(LokiPlaceTrigger.Instance instance, FriendlyByteBuf buffer) {
//        PacketUtil.writeEntityPredicate(instance.getPlayer(), buffer);
//        PacketUtil.writeItemPredicate(instance.getRing(), buffer);
//        PacketUtil.writeIntRange(instance.getBlocksPlaced(), buffer);
//    }
//
//    @Override
//    public LokiPlaceTrigger.Instance read(FriendlyByteBuf buffer) {
//        return new LokiPlaceTrigger.Instance(EntityPredicate.Composite.ANY,
//                PacketUtil.readEntityPredicate(buffer),
//                PacketUtil.readItemPredicate(buffer),
//                PacketUtil.readIntRange(buffer)
//        );
//    }
//}
