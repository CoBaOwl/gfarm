package com.coba.gfarm;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static float ENERGY_MULTIPLAYER = 1.0f;

    public static void init (File file) {
        config = new Configuration(file);

        String category = "General";

        config.addCustomCategoryComment(category, "General configuration variable");
        ENERGY_MULTIPLAYER = config.getFloat("Energy multiplayer", category, 1.0f, 0.5f, 8.0f,"Energy multiplayer for farms");

        config.save();
    }

    public static void registerConfig(FMLPreInitializationEvent event) {
        gfarm.config = new File(event.getModConfigurationDirectory() + "/" + gfarm.MODID);
        gfarm.config.mkdirs();
        init(new File(gfarm.config.getPath(), gfarm.MODID + ".cfg"));
    }

}
