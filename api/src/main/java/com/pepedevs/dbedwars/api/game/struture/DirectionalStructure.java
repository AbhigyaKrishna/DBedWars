package com.pepedevs.dbedwars.api.game.struture;

import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Map;

public interface DirectionalStructure extends Structure {

    Map<Block, XMaterial> getBlocks(BlockFace face);

    void setBlocks(BlockFace face, Map<Block, XMaterial> blocks);

    String toString();
}
