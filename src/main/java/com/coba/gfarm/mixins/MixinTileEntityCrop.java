package com.coba.gfarm.mixins;

import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.utility.debug.IDebuggable;
import org.spongepowered.asm.mixin.Mixin;
import javax.annotation.Nullable;

@Mixin(TileEntityCrop.class)
public abstract class MixinTileEntityCrop extends TileEntityBase implements IAgriCrop, IDebuggable, IAgriDisplayable {
    public boolean isFertile(@Nullable IAgriPlant plant) {
        return plant != null && plant.getGrowthRequirement().isMet(this.getWorld(), this.pos);
    }
}
