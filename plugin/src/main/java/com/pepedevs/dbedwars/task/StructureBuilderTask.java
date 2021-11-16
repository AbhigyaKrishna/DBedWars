package com.pepedevs.dbedwars.task;

import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.struture.DirectionalStructure;
import com.pepedevs.dbedwars.api.game.struture.Structure;
import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.xseries.XBlock;
import me.Abhigya.core.util.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Iterator;
import java.util.Map;

public class StructureBuilderTask implements Workload {

    private long timestamp = System.currentTimeMillis();
    private int tick = -1;

    private Structure structure;
    private boolean isDirectional;
    private BlockFace face;
    private Arena arena;
    private Iterator<Map.Entry<Block, XMaterial>> blockIterator;

    public StructureBuilderTask(Structure structure, Arena arena){
        this.structure = structure;
        this.isDirectional = false;
        this.face = null;
        this.arena = arena;
        this.blockIterator = structure.getBlocks().entrySet().iterator();
    }

    public StructureBuilderTask(DirectionalStructure structure, BlockFace face, Arena arena){
        this.structure = structure;
        this.isDirectional = true;
        this.face = face;
        this.arena = arena;
        this.blockIterator = structure.getBlocks(face).entrySet().iterator();
    }

    @Override
    public void compute() {
        for (int blocksToPlacePerSecond = structure.getBlocksToPlacePerSecond(); blocksToPlacePerSecond > 0; blocksToPlacePerSecond--) {
            if (!blockIterator.hasNext())
                break;

            Map.Entry<Block, XMaterial> entry = blockIterator.next();

            if (!entry.getKey().isEmpty()) continue;

            arena.setBlock(entry.getKey(), entry.getValue().parseMaterial());

            if (isDirectional){
                XBlock.setDirection(entry.getKey(), face);
            }

        }
    }

    @Override
    public boolean reSchedule() {
        return blockIterator.hasNext();
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp < 50)
            return false;
        timestamp = System.currentTimeMillis();
        tick++;

        return blockIterator.hasNext();
    }
}
