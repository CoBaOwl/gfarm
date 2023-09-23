package com.coba.gfarm.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import com.coba.gfarm.textures.GFarmTextures;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy  {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        GFarmTextures.preInit(event);
    }

}
