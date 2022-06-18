package de.melanx.jea;

import de.melanx.jea.network.JustEnoughNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.moddingx.libx.mod.ModX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

@Mod("jea")
public final class JustEnoughAdvancements extends ModX {
    
    public static final Logger logger = LoggerFactory.getLogger("jea");

    private static JustEnoughAdvancements instance;
    private static JustEnoughNetwork network;
    
    public JustEnoughAdvancements() {
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
