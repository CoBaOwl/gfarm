package com.coba.gfarm.integration;

import com.coba.gfarm.block.GFarmStickyBlock;
import com.coba.gfarm.register.Registry;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.misc.IAgriRegistry;
import com.infinityraider.agricraft.api.v1.mutation.IAgriMutationRegistry;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.plugin.AgriPlugin;
import com.infinityraider.agricraft.api.v1.plugin.IAgriPlugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@AgriPlugin
public class GFarmAgriCraftPlugin implements IAgriPlugin {
    @Override
    public boolean isEnabled() {return true;}

    @Override
    public String getId() {return "gfarm";}

    @Override
    public String getName() {return "GregTech Farm AgriCraft Plugin";}

    @Override
    public void registerPlants(@Nonnull IAgriRegistry<IAgriPlant> plantRegistry) {
        for (GFarmStickyBlock crop : Registry.BLOCKS) {
            plantRegistry.add(new GFarmAgriCraftCrop(crop));
        }
    }

    @Override
    public void registerMutations(@Nonnull IAgriMutationRegistry mutationRegistry) {
        List<String> parents = Arrays.asList("vanilla:cactus_plant", "vanilla:sugarcane_plant");
        mutationRegistry.add("sticky_plant",0.3D, "gfarm:sticky_plant", parents);
    }
}
