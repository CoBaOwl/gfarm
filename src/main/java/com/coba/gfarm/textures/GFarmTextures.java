package com.coba.gfarm.textures;

import com.coba.gfarm.gfarm;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = gfarm.MODID, value = Side.CLIENT)
public class GFarmTextures {
    public static SimpleOverlayRenderer ADV_FARM_OVERLAY;

    public static void preInit() {
        ADV_FARM_OVERLAY  = new SimpleOverlayRenderer("machines/overlay_adv_farm");
    }
}
