package com.coba.gfarm.items;

import com.coba.gfarm.gfarm;
import com.coba.gfarm.register.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class GFarmStickyItem extends ItemSeeds {

    public GFarmStickyItem(String name, Block crops, Block soil) {
        super(crops, soil);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setMaxStackSize(64);
        gfarm.proxy.registerItemRender(this, 0, name);
        Registry.ITEMS.add(this);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return Registry.STICKY_BLOCK.getDefaultState();
    }

}
