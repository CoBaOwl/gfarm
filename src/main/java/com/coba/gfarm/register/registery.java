package com.coba.gfarm.register;

import com.coba.gfarm.machines.MetaTileEntityFarm;
import gregtech.api.GTValues;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;

import static gregtech.loaders.recipe.CraftingComponent.*;
import static gregtech.loaders.recipe.MetaTileEntityLoader.registerMachineRecipe;

public class registery {
    public static final MetaTileEntityFarm[] FARM = new MetaTileEntityFarm[2];

    public static void Init() {
        FARM[0] = MetaTileEntities.registerMetaTileEntity(3000, new MetaTileEntityFarm(gregtechId("farm.lv"), 1));
        FARM[1] = MetaTileEntities.registerMetaTileEntity(3001, new MetaTileEntityFarm(gregtechId("farm.mv"), 2));
    }

    private static ResourceLocation gregtechId(String name) {
        return new ResourceLocation(GTValues.MODID, name);
    }

    public static void recipes () {
        registerMachineRecipe(FARM, "WGR", "GMR", "TGT", 'M', HULL, 'W', CIRCUIT, 'G', PUMP, 'R', ROBOT_ARM, 'T', PIPE_LARGE);
    }


}
