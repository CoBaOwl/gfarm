package com.coba.gfarm;

import com.coba.gfarm.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod(modid = gfarm.MODID, name = gfarm.NAME, version = gfarm.VERSION, dependencies = gfarm.DEP_VERSION_STRING)
public class gfarm
{
    public static final String MODID = "gfarm";
    public static final String NAME = "GregTechCEu farm";
    public static final String VERSION = "0.1.9";
    public static final String DEP_VERSION_STRING = "required-after:gregtech";
    private static Logger logger;


    public static File config;
    @SidedProxy(modId = MODID, clientSide = "com.coba.gfarm.common.ClientProxy", serverSide = "com.coba.gfarm.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
