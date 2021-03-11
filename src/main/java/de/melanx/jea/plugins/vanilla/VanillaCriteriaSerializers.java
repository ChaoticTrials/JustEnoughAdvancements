package de.melanx.jea.plugins.vanilla;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.plugins.vanilla.serializer.*;
import net.minecraftforge.event.RegistryEvent;

public class VanillaCriteriaSerializers {
    
    public static void init(RegistryEvent.Register<CriterionSerializer<?>> event) {
        event.getRegistry().registerAll(
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
                new EntityPickupItemSerializer(),
                new UseEnderEyeSerializer(),
                new UseTotemSerializer(),
                new VillagerTradeSerializer()
        );
    }
}
