package de.melanx.jea;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import de.melanx.jea.api.client.Jea;
import de.melanx.jea.network.JustEnoughNetwork;
import de.melanx.jea.plugins.vanilla.TestInfo;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaSerializers;
import de.melanx.jea.plugins.vanilla.render.*;
import de.melanx.jea.render.SpecialModels;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.util.ResourceLocation;
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
        
        Jea.register(new ResourceLocation("inventory_changed"), new TestInfo());
    }
}
