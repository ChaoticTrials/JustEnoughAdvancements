//package de.melanx.jea.plugins.mythicbotany;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.api.client.Jea;
//import de.melanx.jea.plugins.mythicbotany.criteria.AlfRepairInfo;
//import de.melanx.jea.plugins.mythicbotany.criteria.MjoellnirInfo;
//import de.melanx.jea.plugins.mythicbotany.serializer.AlfRepairSerializer;
//import de.melanx.jea.plugins.mythicbotany.serializer.MjoellnirSerializer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.event.RegistryEvent;
//
//public class MythicBotanyPlugin {
//
//    public static void init(RegistryEvent.Register<CriterionSerializer<?>> event) {
//        event.getRegistry().registerAll(
//                new MjoellnirSerializer(),
//                new AlfRepairSerializer()
//        );
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void register() {
//        Jea.register(MythicBotanyCriteriaIds.ALF_REPAIR, new AlfRepairInfo());
//        Jea.register(MythicBotanyCriteriaIds.MJOELLNIR, new MjoellnirInfo());
//    }
//}
