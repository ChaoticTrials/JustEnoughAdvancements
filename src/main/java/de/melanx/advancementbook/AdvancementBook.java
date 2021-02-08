package de.melanx.advancementbook;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AdvancementBook.MODID)
public class AdvancementBook {

    public static final String MODID = "advancementbook";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public AdvancementBook instance;

    public AdvancementBook() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }
}
