package de.melanx.jea.config;

import org.moddingx.libx.annotation.config.RegisterConfig;
import org.moddingx.libx.config.Config;

@RegisterConfig
public class JeaConfig {

    @Config({
            "When this option is disabled, hidden advancements will not display on the client.",
            "This is a server side option and the value set on the server will override the value set on the client"
    })
    public static boolean hiddenAdvancements = true;
}
