package com.coba.gfarm.block;

import com.coba.gfarm.gfarm;
import com.coba.gfarm.register.Registry;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.render.RenderMethod;
import com.infinityraider.agricraft.api.v1.util.FuzzyStack;
import gregtech.common.items.MetaItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Random;

public class GFarmStickyBlock extends BlockCrops {

    private static final AxisAlignedBB[] STICKY_BLOCK =
            new AxisAlignedBB[] {
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D),
                    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };

    public GFarmStickyBlock(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(gfarm.MODID, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
        Registry.BLOCKS.add(this);
    }

    public RenderMethod getRender() {
        return RenderMethod.HASHTAG;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;
        drops.add(Registry.STICKY.getDefaultInstance());
        if (this.isMaxAge(state)) {
            if (rand.nextDouble() <= .8D) drops.add(MetaItems.STICKY_RESIN.getStackForm(1));
        }
        drops.add(new ItemStack(this));
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.withAge(meta);
    }
    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return this.getAge(state);
    }
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }

    public Item getSeed() {return Registry.STICKY.getDefaultInstance().getItem();}
    public Item getCrop() {return MetaItems.STICKY_RESIN.getStackForm().getItem();}
    static public ItemStack getSeedStack() {return new ItemStack(Registry.STICKY);}
    public ItemStack getCropStack() {
        return GameRegistry.makeItemStack("gregtech:meta_item_1", 438, 1, "");
    }
    public String getName() {
        return "sticky_plant";
    }
    public FuzzyStack getRequiredBlock() {
        return new FuzzyStack(OreDictionary.getOres("blockRubber").get(0));
    }
}
