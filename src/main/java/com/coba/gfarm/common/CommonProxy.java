package com.coba.gfarm.common;

import com.coba.gfarm.gfarm;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import com.coba.gfarm.register.Registry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = gfarm.MODID)
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Registry.Init(event);
    }

    public void init() {

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(Registry.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(Registry.BLOCKS.toArray(new Block[0]));
    }
    public void postInit() {
        Registry.recipes();
    }

    public void registerItemRender(Item item, int meta, String id){}
}
