package com.pepedevs.dbedwars.api.game.struture;

import com.pepedevs.corelib.utils.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Map;

public interface DirectionalStructure extends Structure {

    Map<Block, XMaterial> getNorthBlocks();

    Map<Block, XMaterial> getWestBlocks();

    Map<Block, XMaterial> getEastBlocks();

    Map<Block, XMaterial> getSouthBlocks();

    Map<Block, XMaterial> getBlocks(BlockFace face);

    void setNorthBlocks(Map<Block, XMaterial> blocks);

    void setWestBlocks(Map<Block, XMaterial> blocks);

    void setEastBlocks(Map<Block, XMaterial> blocks);

    void setSouthBlocks(Map<Block, XMaterial> blocks);

    void setBlocks(BlockFace face, Map<Block, XMaterial> blocks);
}
