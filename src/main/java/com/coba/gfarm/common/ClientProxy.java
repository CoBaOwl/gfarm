package com.coba.gfarm.common;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
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

    public void registerItemRender(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
