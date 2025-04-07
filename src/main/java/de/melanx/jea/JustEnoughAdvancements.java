package de.melanx.jea;

import de.melanx.jea.network.JustEnoughNetwork;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.moddingx.libx.mod.ModX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

@Mod(JustEnoughAdvancements.MODID)
public final class JustEnoughAdvancements extends ModX {

    public static final String MODID = "jea";
    public static final Logger LOGGER = LoggerFactory.getLogger(JustEnoughAdvancements.class);

    private static JustEnoughAdvancements instance;
    private static JustEnoughNetwork network;

    public JustEnoughAdvancements() {
        instance = this;
        network = new JustEnoughNetwork(this);

        NeoForge.EVENT_BUS.register(new EventHandler());
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
