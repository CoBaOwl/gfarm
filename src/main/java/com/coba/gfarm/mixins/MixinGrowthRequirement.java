package com.coba.gfarm.mixins;

import com.google.common.base.Preconditions;
import com.infinityraider.agricraft.api.v1.requirement.IGrowthRequirement;
import com.infinityraider.agricraft.farming.growthrequirement.GrowthRequirement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(GrowthRequirement.class)
public abstract class MixinGrowthRequirement implements IGrowthRequirement {
    public boolean hasValidLight(@Nonnull World world, @Nonnull BlockPos pos) {
        Preconditions.checkNotNull(world);
        Preconditions.checkNotNull(pos);
        int light = world.getLightFromNeighbors(pos);
        return light >= this.getMinLight() && light <= this.getMaxLight();
    }
}
