package de.melanx.jea;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.config.JeaConfig;
import de.melanx.jea.network.JustEnoughNetwork;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaSerializers;
import de.melanx.jea.plugins.vanilla.criteria.*;
import de.melanx.jea.plugins.vanilla.special.RideStriderInfo;
import de.melanx.jea.render.SpecialModels;
import io.github.noeppi_noeppi.libx.config.ConfigManager;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod("jea")
public class JustEnoughAdvancements extends ModX {

    private static JustEnoughAdvancements instance;
    private static JustEnoughNetwork network;

    public static final Logger logger = LogManager.getLogger();

    public JustEnoughAdvancements() {
        super("jea", null);
        instance = this;
        network = new JustEnoughNetwork(this);

        ConfigManager.registerConfig(this.modid, JeaConfig.class, false);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(JeaRegistries::initRegistries);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(CriterionSerializer.class, VanillaCriteriaSerializers::init);

        DistExecutor.unsafeRunForDist(() -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(SpecialModels::registerModels);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(SpecialModels::bakeModels);
            return null;
        }, () -> () -> null);
        
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Nonnull
    public static JustEnoughAdvancements getInstance() {
        return instance;
    }

    @Nonnull
    public static JustEnoughNetwork getNetwork() {
        return network;
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        //
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
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
