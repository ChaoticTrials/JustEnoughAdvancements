package de.melanx.jea;

import de.melanx.jea.network.JustEnoughNetwork;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        //
    }
}
