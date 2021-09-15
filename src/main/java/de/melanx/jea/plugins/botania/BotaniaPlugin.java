//package de.melanx.jea.plugins.botania;
//
//import de.melanx.jea.api.CriterionSerializer;
//import de.melanx.jea.api.client.Jea;
//import de.melanx.jea.plugins.botania.criteria.*;
//import de.melanx.jea.plugins.botania.serializer.*;
//import de.melanx.jea.plugins.botania.special.*;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.event.RegistryEvent;
//
//public class BotaniaPlugin {
//
//    public static void init(RegistryEvent.Register<CriterionSerializer<?>> event) {
//        event.getRegistry().registerAll(
//                new ActivatePortalSerializer(),
//                new BreadInPortalSerializer(),
//                new CorporeaRequestSerializer(),
//                new GaiaNoArmorSerializer(),
//                new LokiPlaceSerializer(),
//                new ManaGunSerializer(),
//                new RelicRedeemSerializer(),
//                new UseItemSuccessSerializer()
//        );
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void register() {
//        Jea.register(BotaniaCriteriaIds.ACTIVATE_PORTAL, new ActivatePortalInfo());
//        Jea.register(BotaniaCriteriaIds.BREAD_IN_PORTAL, new BreadInPortalInfo());
//        Jea.register(BotaniaCriteriaIds.CORPOREA_REQUEST, new CorporeaRequestInfo());
//        Jea.register(BotaniaCriteriaIds.GAIA_NO_ARMOR, new GaiaNoArmorInfo());
//        Jea.register(BotaniaCriteriaIds.LOKI_PLACE, new LokiPlaceInfo());
//        Jea.register(BotaniaCriteriaIds.MANA_GUN, new ManaGunInfo());
//        Jea.register(BotaniaCriteriaIds.RELIC_REDEEM, new RelicRedeemInfo());
//        Jea.register(BotaniaCriteriaIds.USE_ITEM_SUCCESS, new UseItemSuccessInfo());
//        
//        Jea.registerSpecial(PinkinatorInfo.ADVANCEMENT, PinkinatorInfo.CRITERION, new PinkinatorInfo());
//        Jea.registerSpecial(MoveSpawnerInfo.ADVANCEMENT, MoveSpawnerInfo.CRITERION, new MoveSpawnerInfo());
//        Jea.registerSpecial(AreaMineInfo.ADVANCEMENT, AreaMineInfo.CRITERION, new AreaMineInfo());
//        Jea.registerSpecial(FormEnchanterInfo.ADVANCEMENT, FormEnchanterInfo.CRITERION, new FormEnchanterInfo());
//        Jea.registerSpecial(PetPotatoInfo.ADVANCEMENT, PetPotatoInfo.CRITERION, new PetPotatoInfo());
//        Jea.registerSpecial(LaputaInfo.ADVANCEMENT, LaputaInfo.CRITERION, new LaputaInfo());
//        Jea.registerSpecial(BaubleInfo.ADVANCEMENT, BaubleInfo.CRITERION, new BaubleInfo());
//        Jea.registerSpecial(LuminizerInfo.ADVANCEMENT, LuminizerInfo.CRITERION, new LuminizerInfo());
//    }
//}
