package com.coba.gfarm.mixins;

import com.agricraft.agricore.core.AgriCore;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.items.ItemClipping;
import com.infinityraider.infinitylib.item.IAutoRenderedItem;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.utility.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemClipping.class)
public abstract class MixinItemClipping extends ItemBase implements IAutoRenderedItem {
    public MixinItemClipping() {
        super("clipping");
        this.setCreativeTab(null);
    }
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        // If in creative or remote, skip.
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        // Get the item & seed.
        final ItemStack stack = player.getHeldItem(hand);
        final AgriSeed seed = AgriApi.getSeedRegistry().valueOf(stack).orElse(null);

        // If seed is missing, error and pass.
        if (seed == null) {
            AgriCore.getLogger("agricraft").info("Unable to resolve an ItemClipping to an instance of an AgriSeed!");
            return EnumActionResult.PASS;
        }

        // Look for a crop instance at the given location.
        final IAgriCrop crop = WorldHelper.getTile(world, pos, IAgriCrop.class).orElse(null);

        // If the crop is missing, does not accept the given seed, or is not fertile for the seed, pass.
        if (crop == null || !crop.acceptsSeed(seed) || !crop.isFertile(seed)) {
            return EnumActionResult.PASS;
        }

        // Return that nothing happened.
        return EnumActionResult.PASS;
    }
}
