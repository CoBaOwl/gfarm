package com.coba.gfarm.machines;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import com.coba.gfarm.ConfigHandler;
import com.coba.gfarm.logic.PlantNavigator;
import com.coba.gfarm.textures.GFarmTextures;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.infinitylib.utility.WorldHelper;
import gregtech.api.GTValues;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IControllable;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

public class MetaTileEntityFarm extends TieredMetaTileEntity implements IControllable {

    private final int inventorySize;
    private final long energyAmountPerOperation;
    private static final int PLANT_CHECK_SIZE = 81;
    private final long harvestTicks;
    private final PlantNavigator coodr;
    private boolean workingEnabled;

    private final NonNullList<ItemStack> itemDropCrops;
    public MetaTileEntityFarm(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
        this.workingEnabled = true;
        this.inventorySize = (tier + 1) * (tier + 1);
        this.energyAmountPerOperation = (long) (GTValues.V[tier] * ConfigHandler.ENERGY_MULTIPLAYER);
        this.harvestTicks = 100L;
        this.itemDropCrops = NonNullList.create();
        this.coodr = new PlantNavigator((int)Math.sqrt(PLANT_CHECK_SIZE), tier);
        initializeInventory();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityFarm(metaTileEntityId, getTier());
    }

    @Override
    public void update() {
        super.update();
        if (this.workingEnabled) {
            if (!getWorld().isRemote && (energyContainer.getEnergyStored() >= energyAmountPerOperation) && (getOffsetTimer() % harvestTicks == 0L) && this.itemDropCrops.isEmpty()) {
                this.coodr.setCenter(this.getPos());
                WorldServer world = (WorldServer) this.getWorld();
                while (this.itemDropCrops.isEmpty() && !this.coodr.getPlantFinished()) {
                    NonNullList<BlockPos> plantPosition = this.coodr.plantIter();
                    for(BlockPos itr: plantPosition) {
                        IBlockState plantState = world.getBlockState(itr);
                        if ((plantState.getBlock() instanceof BlockCrops) && (((BlockCrops) plantState.getBlock()).isMaxAge(plantState))) {
                            plantState.getBlock().getDrops(this.itemDropCrops, world, itr, plantState, 0);
                            for (int i = 0; i < this.itemDropCrops.size(); i++)
                                if (this.itemDropCrops.get(i).getItem() instanceof IPlantable) {
                                    this.itemDropCrops.remove(i);
                                    break;
                                }
                            world.setBlockState(itr, plantState.getBlock().getDefaultState());
                        }
                        if (Loader.isModLoaded("agricraft")) {
                            final Optional<IAgriCrop> agriCrop = WorldHelper.getTile(world, itr, IAgriCrop.class);
                            agriCrop.ifPresent(iAgriCrop -> iAgriCrop.onHarvest(this.itemDropCrops::add, null));
                        }
                    }
                }
                this.coodr.resetPlantFinished();
            }

            if (!this.itemDropCrops.isEmpty() && GTTransferUtils.addItemsToItemHandler(exportItems, true, this.itemDropCrops)) {
                GTTransferUtils.addItemsToItemHandler(exportItems, false, this.itemDropCrops);
                this.itemDropCrops.clear();
                energyContainer.removeEnergy(energyAmountPerOperation);
            }


            if (!getWorld().isRemote && getOffsetTimer() % 5 == 0) {
                pushItemsIntoNearbyHandlers(getFrontFacing());
            }
        }

    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        Textures.SCREEN.renderSided(EnumFacing.UP, renderState, translation, pipeline);
//        Textures.PIPE_OUT_OVERLAY.renderSided(getFrontFacing(), renderState, translation, pipeline);
        ColourMultiplier multiplier = new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()));
        IVertexOperation[] coloredPipeline = ArrayUtils.add(pipeline, multiplier);
        for (EnumFacing renderSide : EnumFacing.HORIZONTALS) {
            if (renderSide == getFrontFacing()) {
                Textures.PIPE_OUT_OVERLAY.renderSided(renderSide, renderState, translation, pipeline);
            } else {
                GFarmTextures.ADV_FARM_OVERLAY.renderSided(renderSide, renderState, translation, coloredPipeline);
            }
        }
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        int rowSize = (int) Math.sqrt(inventorySize);

        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176,
                        18 + 18 * rowSize + 94)
                .label(10, 5, getMetaFullName());

        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                builder.widget(new SlotWidget(exportItems, index, 89 - rowSize * 9 + x * 18, 18 + y * 18, true, false)
                        .setBackgroundTexture(GuiTextures.SLOT));
            }
        }

        builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7, 18 + 18 * rowSize + 12);
        return builder.build(getHolder(), entityPlayer);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new ItemStackHandler(inventorySize);
    }

    @Override
    public boolean isWorkingEnabled() { return workingEnabled; }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        this.workingEnabled = isWorkingAllowed;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("workingEnabled", workingEnabled);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        if (data.hasKey("workingEnabled")) {
            this.workingEnabled = data.getBoolean("workingEnabled");
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeBoolean(workingEnabled);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.workingEnabled = buf.readBoolean();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {
        if (capability == GregtechTileCapabilities.CAPABILITY_CONTROLLABLE) {
            return GregtechTileCapabilities.CAPABILITY_CONTROLLABLE.cast(this);
        }
        return super.getCapability(capability, side);
    }
}
