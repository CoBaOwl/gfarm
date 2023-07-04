package com.coba.gfarm.common;

import com.coba.gfarm.gfarm;
import net.minecraftforge.fml.common.Mod;
import com.coba.gfarm.register.registery;

@Mod.EventBusSubscriber(modid = gfarm.MODID)
public class CommonProxy {
    public void preInit() {
        registery.Init();
    }

    public void init() {

    }

    public void postInit() {
        registery.recipes();
    }
}
