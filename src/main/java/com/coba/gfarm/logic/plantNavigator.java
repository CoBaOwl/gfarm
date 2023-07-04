package com.coba.gfarm.logic;

import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class plantNavigator {
    private final int plantSquareSize;
    private BlockPos plantCenter;
    private BlockPos currentPosition;
    boolean plantFinished;
    int sizeY;

    public plantNavigator(int plantSquareSize, int tier) {
        this.plantFinished = false;
        this.plantSquareSize = plantSquareSize;
        this.sizeY = tier - 1;
    }

    public boolean getPlantFinished () {
        return this.plantFinished;
    }

    public void resetPlantFinished () {
        this.plantFinished = false;
    }

    public void setCenter (BlockPos plantCenter) {
        if (this.plantCenter != plantCenter) {
            this.plantCenter = plantCenter;
            this.currentPosition = plantCenter.add(-(plantSquareSize / 2), 0, -(plantSquareSize / 2));
        }
    }

    public NonNullList<BlockPos> plantIter() {
        NonNullList<BlockPos> result = NonNullList.create();
        if (this.currentPosition.getX() != this.plantCenter.getX() + this.plantSquareSize / 2) {
            this.currentPosition = this.currentPosition.add(1, 0, 0);
        } else {
            this.currentPosition = this.currentPosition.add(-(this.plantSquareSize - 1), 0, 0);
            if (this.currentPosition.getZ() != this.plantCenter.getZ() + this.plantSquareSize / 2) {
                this.currentPosition = this.currentPosition.add(0, 0, 1);
            } else {
                this.currentPosition = this.currentPosition.add(0, 0, -(this.plantSquareSize - 1));
                this.plantFinished = true;
            }
        }
        result.add(this.currentPosition);
        for (int i = this.sizeY; i > 0; i--) {
            result.add(this.currentPosition.add(0, i,0));
            result.add(this.currentPosition.add(0, -i,0));
        }
        return result;
    }
}
