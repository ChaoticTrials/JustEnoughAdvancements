package de.melanx.jea.config;

import io.github.noeppi_noeppi.libx.config.Config;

public class JeaConfig {

    @Config({
            "When this option is disabled, hidden advancements will not display on the client.",
            "This is a server side option and the value set on the server will override the value set on the client"
    })
    public static boolean hiddenAdvancements = true;
}
