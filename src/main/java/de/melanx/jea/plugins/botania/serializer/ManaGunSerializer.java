//package de.melanx.jea.plugins.botania.serializer;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.network.PacketUtil;
//import de.melanx.jea.plugins.botania.BotaniaCriteriaIds;
//import net.minecraft.advancements.critereon.EntityPredicate;
//import net.minecraft.advancements.critereon.ItemPredicate;
//import net.minecraft.network.FriendlyByteBuf;
//import vazkii.botania.common.advancements.ManaGunTrigger;
//
//public class ManaGunSerializer extends CriterionSerializer<ManaGunTrigger.Instance> {
//
//    public ManaGunSerializer() {
//        super(ManaGunTrigger.Instance.class);
//        this.setRegistryName(BotaniaCriteriaIds.MANA_GUN);
//    }
//
//    @Override
//    public void write(ManaGunTrigger.Instance instance, FriendlyByteBuf buffer) {
//        PacketUtil.writeItemPredicate(instance.getItem(), buffer);
//        PacketUtil.writeEntityPredicate(instance.getUser(), buffer);
//        if (instance.getDesu() == null) {
//            buffer.writeByte(0);
//        } else {
//            buffer.writeByte((1 << 1) | (instance.getDesu() ? 1 : 0));
//        }
//    }
//
//    @Override
//    public ManaGunTrigger.Instance read(FriendlyByteBuf buffer) {
//        ItemPredicate item = PacketUtil.readItemPredicate(buffer);
//        EntityPredicate entity = PacketUtil.readEntityPredicate(buffer);
//        byte desu = buffer.readByte();
//        return new ManaGunTrigger.Instance(EntityPredicate.Composite.ANY, item, entity,
//                ((desu & (1 << 1)) != 0) ? ((desu & 1) != 0) : null
//        );
//    }
//}
