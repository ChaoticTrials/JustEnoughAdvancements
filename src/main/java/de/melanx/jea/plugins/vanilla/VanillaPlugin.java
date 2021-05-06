package de.melanx.jea.plugins.vanilla;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.plugins.vanilla.criteria.*;
import de.melanx.jea.plugins.vanilla.serializer.*;
import de.melanx.jea.plugins.vanilla.special.RideStriderInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class VanillaPlugin {

    public static void init(RegistryEvent.Register<CriterionSerializer<?>> event) {
        event.getRegistry().registerAll(
                new ImpossibleSerializer(),
                new DestroyBeeNestSerializer(),
                new BreedAnimalsSerializer(),
                new BrewPotionSerializer(),
                new ChangeDimensionSerializer(),
                new ChangeEffectsSerializer(),
                new ChannelingLightningSerializer(),
                new ConstructBeaconSerializer(),
                new ConsumeItemSerializer(),
                new CureZombieVillagerSerializer(),
                new EnchantItemSerializer(),
                new EnterBlockSerializer(),
                new HurtByEntitySerializer(),
                new FillBucketSerializer(),
                new FishingRodHookSerializer(),
                new InventoryChangeSerializer(),
                new ItemDurabilitySerializer(),
                new KilledByCrossbowSerializer(),
                new KilledByEntitySerializer(),
                new KilledEntitySerializer(),
                new LevitationSerializer(),
                new NetherTravelSerializer(),
                new PlaceBlockSerializer(),
                new EntityInteractionSerializer(),
                new GenerateContainerLootSerializer(),
                new HurtEntitySerializer(),
                new LocationSerializer(),
                new SleepInBedSerializer(),
                new WinRaidSerializer(),
                new RightClickBlockSerializer(),
                new ShootCrossbowSerializer(),
                new SlideBlockSerializer(),
                new SummonEntitySerializer(),
                new TameAnimalSerializer(),
                new TargetHitSerializer(),
                new UseTotemSerializer(),
                new VillagerTradeSerializer()
        );
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void register() {
        Jea.register(VanillaCriteriaIds.DESTROY_BEE_NEST, new DestroyBeeNestInfo());
        Jea.register(VanillaCriteriaIds.BREED_ANIMALS, new BreedAnimalsInfo());
        Jea.register(VanillaCriteriaIds.BREW_POTION, new BrewPotionInfo());
        Jea.register(VanillaCriteriaIds.CHANGE_DIMENSION, new ChangeDimensionInfo());
        Jea.register(VanillaCriteriaIds.CHANNELING_LIGHTNING, new ChannelingLightningInfo());
        Jea.register(VanillaCriteriaIds.CONSTRUCT_BEACON, new ConstructBeaconInfo());
        Jea.register(VanillaCriteriaIds.CONSUME_ITEM, new ConsumeItemInfo());
        Jea.register(VanillaCriteriaIds.CURE_ZOMBIE_VILLAGER, new CureZombieVillagerInfo());
        Jea.register(VanillaCriteriaIds.CHANGE_EFFECTS, new ChangeEffectsInfo());
        Jea.register(VanillaCriteriaIds.ENCHANT_ITEM, new EnchantItemInfo());
        Jea.register(VanillaCriteriaIds.ENTER_BLOCK, new EnterBlockInfo());
        Jea.register(VanillaCriteriaIds.HURT_BY_ENTITY, new HurtByEntityInfo());
        Jea.register(VanillaCriteriaIds.FILL_BUCKET, new FillBucketInfo());
        Jea.register(VanillaCriteriaIds.FISHING_ROD_HOOK, new FishingRodHookInfo());
        Jea.register(VanillaCriteriaIds.INVENTORY_CHANGE, new InventoryChangeInfo());
        Jea.register(VanillaCriteriaIds.ITEM_DURABILITY, new ItemDurabilityInfo());
        Jea.register(VanillaCriteriaIds.KILLED_BY_CROSSBOW, new KilledByCrossbowInfo());
        Jea.register(VanillaCriteriaIds.LEVITATION, new LevitationInfo());
        Jea.register(VanillaCriteriaIds.NETHER_TRAVEL, new NetherTravelInfo());
        Jea.register(VanillaCriteriaIds.PLACE_BLOCK, new PlaceBlockInfo());
        Jea.register(VanillaCriteriaIds.ENTITY_INTERACTION, new EntityInteractionInfo());
        Jea.register(VanillaCriteriaIds.GENERATE_CONTAINER_LOOT, new GenerateContainerLootInfo());
        Jea.register(VanillaCriteriaIds.LOCATION, new LocationInfo());
        Jea.register(VanillaCriteriaIds.SLEEP_IN_BED, new SleepInBedInfo());
        Jea.register(VanillaCriteriaIds.WIN_RAID, new WinRaidInfo());
        Jea.register(VanillaCriteriaIds.RIGHT_CLICK_BLOCK, new RightClickBlockInfo());
        Jea.register(VanillaCriteriaIds.SHOOT_CROSSBOW, new ShootCrossbowInfo());
        Jea.register(VanillaCriteriaIds.SLIDE_BLOCK, new SlideBlockInfo());
        Jea.register(VanillaCriteriaIds.SUMMON_ENTITY, new SummonEntityInfo());
        Jea.register(VanillaCriteriaIds.TAME_ANIMAL, new TameAnimalInfo());
        Jea.register(VanillaCriteriaIds.TARGET_HIT, new TargetHitInfo());
        Jea.register(VanillaCriteriaIds.USE_TOTEM, new UseTotemInfo());
        Jea.register(VanillaCriteriaIds.VILLAGER_TRADE, new VillagerTradeInfo());
        Jea.register(VanillaCriteriaIds.HURT_ENTITY, new HurtEntityInfo());
        Jea.register(VanillaCriteriaIds.KILLED_BY_ENTITY, new KilledByEntityInfo());
        Jea.register(VanillaCriteriaIds.KILLED_ENTITY, new KilledEntityInfo());

        Jea.registerSpecial(RideStriderInfo.ADVANCEMENT, RideStriderInfo.CRITERION, new RideStriderInfo());
    }
}
