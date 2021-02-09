package de.melanx.jea;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JustEnoughAdvancements.MODID)
public class JustEnoughAdvancements {

    public static final String MODID = "jea";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public JustEnoughAdvancements instance;

    public JustEnoughAdvancements() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }
}
