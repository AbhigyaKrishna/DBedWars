package com.pepedevs.dbedwars.api.game.struture;

import org.bukkit.block.Block;

import java.util.List;

public interface Structure {

    List<Block> getBlocks();

    void setBlocks(List<Block> blocks);

    int getBlocksToPlacePerSecond();
}
