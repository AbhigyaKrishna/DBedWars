package com.pepedevs.dbedwars.api.game.struture;

import com.pepedevs.corelib.utils.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Map;

public interface DirectionalStructure extends Structure {

    Map<Block, XMaterial> getNorthBlocks();

    void setNorthBlocks(Map<Block, XMaterial> blocks);

    Map<Block, XMaterial> getWestBlocks();

    void setWestBlocks(Map<Block, XMaterial> blocks);

    Map<Block, XMaterial> getEastBlocks();

    void setEastBlocks(Map<Block, XMaterial> blocks);

    Map<Block, XMaterial> getSouthBlocks();

    void setSouthBlocks(Map<Block, XMaterial> blocks);

    Map<Block, XMaterial> getBlocks(BlockFace face);

    void setBlocks(BlockFace face, Map<Block, XMaterial> blocks);

}
