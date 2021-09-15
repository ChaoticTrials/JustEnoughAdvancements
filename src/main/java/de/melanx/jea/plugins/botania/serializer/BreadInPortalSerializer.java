//package de.melanx.jea.plugins.botania.serializer;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.network.PacketUtil;
//import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.network.FriendlyByteBuf;
//import vazkii.botania.common.advancements.AlfPortalBreadTrigger;
//
//public class BreadInPortalSerializer extends CriterionSerializer<AlfPortalBreadTrigger.Instance> {
//
//    public BreadInPortalSerializer() {
//        super(AlfPortalBreadTrigger.Instance.class);
//        this.setRegistryName(BotaniaCriteriaIds.BREAD_IN_PORTAL);
//    }
//
//    @Override
//    public void write(AlfPortalBreadTrigger.Instance instance, FriendlyByteBuf buffer) {
//        PacketUtil.writeLocationPredicate(instance.getPortal(), buffer);
//    }
//
//    @Override
//    public AlfPortalBreadTrigger.Instance read(FriendlyByteBuf buffer) {
//        return new AlfPortalBreadTrigger.Instance(EntityPredicate.Composite.ANY, PacketUtil.readLocationPredicate(buffer));
//    }
//}
