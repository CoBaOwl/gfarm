package com.coba.gfarm.mixins;

import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.items.ItemJournal;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.agricraft.tiles.analyzer.TileEntitySeedAnalyzer;
import com.infinityraider.infinitylib.block.tile.TileEntityRotatableBase;
import com.infinityraider.infinitylib.utility.inventory.IInventoryItemHandler;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.infinityraider.agricraft.tiles.analyzer.TileEntitySeedAnalyzer.JOURNAL_SLOT_ID;
import static com.infinityraider.agricraft.tiles.analyzer.TileEntitySeedAnalyzer.SPECIMEN_SLOT_ID;

@Mixin(TileEntitySeedAnalyzer.class)
public abstract class MixinTileEntitySeedAnalyzer extends TileEntityRotatableBase implements ISidedInventory, ITickable, IAgriDisplayable, IInventoryItemHandler {
    private int progress = 0;
    @Nonnull
    private ItemStack specimen = ItemStack.EMPTY;
    @Nonnull
    private ItemStack journal = ItemStack.EMPTY;
    public final int maxProgress() {
        return 100;
    }
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        // Step 0: Convert null to empty stack.
        if (stack == null) {
            stack = ItemStack.EMPTY;
        }
        // Step 1: Update the appropriate slot.
        switch (slot) {
            case SPECIMEN_SLOT_ID:
                this.specimen = stack;
                if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
                    stack.setCount(getInventoryStackLimit());
                }
                this.progress = isSpecimenAnalyzed() ? maxProgress() : 0;
                break;
            case JOURNAL_SLOT_ID:
                this.journal = stack;
                break;
        }

        // Step 2: If both an analyzed plant and a journal are present, then make sure there's an entry recorded.
        final Optional<AgriSeed> seed = AgriApi.getSeedRegistry().valueOf(specimen);
        if (seed.isPresent()) {
            // Reconvert the seed.
//            this.specimen = seed.get().toStack(specimen.getCount());

            if (this.hasJournal() && seed.get().getStat().isAnalyzed()) {
                // The add method tests if the journal already has the plant before adding it.
                ((ItemJournal) journal.getItem()).addEntry(journal, seed.get().getPlant());
            }
        }

        // Step 3: Finish up.
        this.markForUpdate();
    }

    public final boolean isSpecimenAnalyzed() {
        if (!this.specimen.isEmpty()) {
            Optional<AgriSeed> seed = AgriApi.getSeedRegistry().valueOf(specimen);
            return seed.isPresent() && seed.get().getStat().isAnalyzed();
        }
        return false;
    }
    public final boolean hasJournal() {
        return (!this.journal.isEmpty() && this.journal.getItem() != null);
    }
}
