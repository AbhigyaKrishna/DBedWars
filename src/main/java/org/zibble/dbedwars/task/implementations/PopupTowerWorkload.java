package org.zibble.dbedwars.task.implementations;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.item.PopupTowerChestItem;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PopupTowerWorkload implements Workload {

    private final XMaterial material;
    private final SoundVP sound;
    private final ParticleEffectASC particle;
    private final Block chest;
    private final DyeColor color;
    private final BlockFace face;
    private final int blocksPerTick;
    private final Arena arena;
    private HashMap<Block, Material> blockMap;
    private Block[] blocks;
    private long timestamp = System.currentTimeMillis();
    private int t = -1;

    public PopupTowerWorkload(
            XMaterial material,
            SoundVP soundVP,
            ParticleEffectASC particleWithoutLocation,
            Block chest,
            Team team,
            int blocksPerTick) {
        this.material = material;
        this.sound = soundVP;
        this.particle = particleWithoutLocation;
        this.chest = chest;
        this.color = team.getColor().getDyeColor();
        this.blocksPerTick = blocksPerTick;
        this.face = (((Directional) chest.getState().getData()).getFacing().getOppositeFace());
        this.arena = team.getArena();
        initBlockMap();
    }

    private void initBlockMap() {
        this.blockMap = new LinkedHashMap<>();
        switch (face) {
            case EAST: {
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getEastBlocks(chest, material);
                break;
            }
            case WEST: {
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getWestBlocks(chest, material);
                break;
            }
            case NORTH: {
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getNorthBlocks(chest, material);
                break;
            }
            case SOUTH: {
                this.blockMap = PopupTowerChestItem.PopupTowerBlocks.getSouthBlocks(chest, material);
                break;
            }
        }
        this.blocks = blockMap.keySet().toArray(new Block[0]);
    }

    @Override
    public void compute() {
        for (int i = 0; i < blocksPerTick; i++) {
            if ((t * blocksPerTick + i) <= blocks.length - 1) {
                Block block = blocks[t * blocksPerTick + i];
                Material material = blockMap.get(block);
                SchedulerUtils.runTask(() -> {
                    if (block.isEmpty()) {
                        arena.setBlock(block, XMaterial.matchXMaterial(blockMap.get(block)));
                        if (block.getState().getData() instanceof Colorable) {
                            XBlock.setColor(block, color);
                        }
                        if (material == Material.LADDER) {
                            XBlock.setDirection(block, face);
                        }
                    }
                });
                sound.play(block.getLocation());
                particle.build().setLocation(block.getLocation()).display();
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
