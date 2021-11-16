package com.pepedevs.dbedwars.task;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.xseries.XBlock;
import me.Abhigya.core.util.xseries.XMaterial;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.item.PopupTowerChestItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PopupTowerWorkload implements Workload {

    private final XMaterial material;
    private final SoundVP sound;
    private final ParticleBuilder particle;
    private final Block chest;
    private final DyeColor color;
    private HashMap<Block, Material> blockMap;
    private final BlockFace face;
    private Block[] blocks;
    private final int blocksPerTick;
    private final Arena arena;

    public PopupTowerWorkload(
            XMaterial material,
            SoundVP soundVP,
            ParticleBuilder particleWithoutLocation,
            Block chest,
            ArenaPlayer player,
            int blocksPerTick) {
        this.material = material;
        this.sound = soundVP;
        this.particle = particleWithoutLocation;
        this.chest = chest;
        this.color = player.getTeam().getColor().getDyeColor();
        this.blocksPerTick = blocksPerTick;
        this.face = (((Directional) chest.getState().getData()).getFacing().getOppositeFace());
        this.arena = player.getArena();
        initBlockMap();
    }

    private void initBlockMap() {
        this.blockMap = new LinkedHashMap<>();
        switch (face) {
            case EAST:
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getEastBlocks(chest, material);
                break;
            case WEST:
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getWestBlocks(chest, material);
                break;
            case NORTH:
                this.blockMap =
                        PopupTowerChestItem.PopupTowerBlocks.getNorthBlocks(chest, material);
                break;
            case SOUTH:
                this.blockMap =
                        PopupTowerChestItem.PopupTowerBlocks.getSouthBlocks(chest, material);
                break;
        }
        blocks = blockMap.keySet().toArray(new Block[0]);
    }

    private long timestamp = System.currentTimeMillis();
    private int t = -1;

    @Override
    public void compute() {
        for (int i = 0; i < blocksPerTick; i++) {
            if ((t * blocksPerTick + i) <= blocks.length - 1) {
                Block block = blocks[t * blocksPerTick + i];
                Material material = blockMap.get(block);
                if (block.isEmpty()) {
                    arena.setBlock(block, blockMap.get(block));
                    if (block.getState().getData() instanceof Colorable) {
                        XBlock.setColor(block, color);
                    }
                    if (material == Material.LADDER) {
                        XBlock.setDirection(block, face);
                    }
                }
                sound.play(block.getLocation());
                particle.setLocation(block.getLocation()).display();
            }
        }
    }

    @Override
    public boolean reSchedule() {
        return t * blocksPerTick < blocks.length + blocksPerTick;
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp <= 50) {
            return false;
        }
        timestamp = System.currentTimeMillis();
        t++;
        return t * blocksPerTick < blocks.length + blocksPerTick;
    }
}