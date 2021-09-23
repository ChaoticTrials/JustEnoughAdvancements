package de.melanx.jea;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import de.melanx.jea.network.JustEnoughNetwork;
import de.melanx.jea.plugins.vanilla.VanillaPlugin;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod("jea")
public final class JustEnoughAdvancements extends ModX {

    private static JustEnoughAdvancements instance;
    private static JustEnoughNetwork network;

    public static final Logger logger = LogManager.getLogger();

    public JustEnoughAdvancements() {
        super("jea", null);
        instance = this;
        network = new JustEnoughNetwork(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(JeaRegistries::initRegistries);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(CriterionSerializer.class, VanillaPlugin::init);
        if (ModList.get().isLoaded("botania")) {
//            FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(CriterionSerializer.class, BotaniaPlugin::init);
        }
        if (ModList.get().isLoaded("mythicbotany")) {
//            FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(CriterionSerializer.class, MythicBotanyPlugin::init);
        }
        
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
        VanillaPlugin.register();
        if (ModList.get().isLoaded("botania")) {
//            BotaniaPlugin.register();
        }
        if (ModList.get().isLoaded("mythicbotany")) {
//            MythicBotanyPlugin.register();
        }
    }
    
    public void checkRegistryResourceLocation(ResourceLocation location, String where) {
        // Jea may register serializers and criterion infos for every other mod.
        String namespace = ModLoadingContext.get().getActiveNamespace();
        if (namespace != null && !namespace.equals("minecraft") && !namespace.equals(this.modid)
                && !namespace.equals(location.getNamespace())) {
            logger.info("Potentially Dangerous alternative prefix `{}` for name `{}`, expected `{}` for {}.", location.getNamespace(), location.getPath(), namespace, where);
        }
    }
}
