package com.pepedevs.dbedwars.api.game.struture;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;

public interface DirectionalStructure extends Structure {

    List<Block> getNorthBlocks();

    List<Block> getWestBlocks();

    List<Block> getEastBlocks();

    List<Block> getSouthBlocks();

    List<Block> getBlocks(BlockFace face);

    void setNorthBlocks(List<Block> blocks);

    void setWestBlocks(List<Block> blocks);

    void setEastBlocks(List<Block> blocks);

    void setSouthBlocks(List<Block> blocks);

    void setBlocks(BlockFace face, List<Block> blocks);

}
