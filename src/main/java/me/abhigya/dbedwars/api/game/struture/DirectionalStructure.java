package me.abhigya.dbedwars.api.game.struture;

import org.bukkit.block.Block;

import java.util.List;

public interface DirectionalStructure extends Structure {

    List<Block> getNorthBlocks();

    List<Block> getWestBlocks();

    List<Block> getEastBlocks();

    List<Block> getSouthBlocks();
}
