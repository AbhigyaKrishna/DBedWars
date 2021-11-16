package com.pepedevs.dbedwars.api.game.struture;

import me.Abhigya.core.util.xseries.XMaterial;
import org.bukkit.block.Block;

import java.util.Map;

public interface Structure {

    Map<Block, XMaterial> getBlocks();

    void setBlocks(Map<Block, XMaterial> blocks);

    int getBlocksToPlacePerSecond();

    void setBlocksToPlacePerSecond(int n);

    Block getCentre();

    void setCentre(Block centre);
}
