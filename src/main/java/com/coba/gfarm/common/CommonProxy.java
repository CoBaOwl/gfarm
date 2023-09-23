package com.coba.gfarm.common;

import com.coba.gfarm.gfarm;
import net.minecraftforge.fml.common.Mod;
import com.coba.gfarm.register.Registry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber(modid = gfarm.MODID)
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Registry.Init(event);
    }

    public void init() {

    }

    public void postInit() {
        Registry.recipes();
    }
}
