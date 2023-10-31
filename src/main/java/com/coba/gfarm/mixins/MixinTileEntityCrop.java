package com.coba.gfarm.mixins;

import com.google.common.base.Preconditions;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.init.AgriItems;
import com.infinityraider.agricraft.items.ItemClipping;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.utility.debug.IDebuggable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(TileEntityCrop.class)
public abstract class MixinTileEntityCrop extends TileEntityBase implements IAgriCrop, IDebuggable, IAgriDisplayable {
    private AgriSeed seed;
    private int growthStage;
    public boolean isFertile(@Nullable IAgriPlant plant) {
        return plant != null && plant.getGrowthRequirement().isMet(this.getWorld(), this.pos);
    }

    public void getDrops(@Nonnull Consumer<ItemStack> consumer, boolean includeCropSticks, boolean includeSeeds, boolean includeProducts) {
        // Check that the consumer is not null.
        Preconditions.checkNotNull(consumer);

        // Perform crop stick drop.
        if (includeCropSticks) {
            consumer.accept(new ItemStack(AgriItems.getInstance().CROPS, this.isCrossCrop() ? 2 : 1));
        }
        if (this.hasSeed()) {
            if (includeSeeds && this.seed.getPlant().getSeedDropChanceBase() + this.growthStage * this.seed.getPlant().getSeedDropChanceBonus() > this.getRandom().nextDouble()) {
                seed = this.seed.withStat(this.seed.getStat());
                consumer.accept(ItemClipping.getClipping(seed, 1));
            }
            if (includeProducts && this.isMature()) {
                for (int trials = (this.seed.getStat().getGain() + 3) / 3; trials > 0; trials--) {
                    this.seed.getPlant().getHarvestProducts(consumer, this, this.seed.getStat(), this.getRandom());
                }
            }
        }
    }
}
