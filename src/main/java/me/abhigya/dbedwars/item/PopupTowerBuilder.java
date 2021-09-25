package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.xseries.XBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class PopupTowerBuilder {

    private Location location;
    private BlockFace face;
    private LinkedHashMap<Block, Material> blockMap;
    private DyeColor color;


    public PopupTowerBuilder(Location location, BlockFace face, DyeColor color){
        this.location = location;
        this.face = face;
        this.color = color;
        initBlockMap();
    }

    private void initBlockMap(){
        this.blockMap = new LinkedHashMap<>();
        switch (this.face){
            case EAST:
                this.blockMap = PopupTowerBlocks.getEastBlocks(location.getBlock());
                break;
            case WEST:
                this.blockMap = PopupTowerBlocks.getWestBlocks(location.getBlock());
                break;
            case NORTH:
                this.blockMap = PopupTowerBlocks.getNorthBlocks(location.getBlock());
                break;
            case SOUTH:
                this.blockMap = PopupTowerBlocks.getSouthBlocks(location.getBlock());
                break;
        }
    }

    public Workload getWorkload(int blocksPerTick){
        return new Workload() {

            final Block[] blocks = blockMap.keySet().toArray(new Block[0]);
            long timestamp = System.currentTimeMillis();
            int t = 0;

            @Override
            public void compute() {
                timestamp = System.currentTimeMillis();
                for (int i = 0; i < blocksPerTick; i++) {
                    if ((t*blocksPerTick + i)<=blocks.length-1){
                        Block block = blocks[t*blocksPerTick + i];
                        Material material = blockMap.get(block);
                        if (block.isEmpty()){
                            block.setType(blockMap.get(block));
                            if(material == Material.WOOL){
                                XBlock.setColor(block,color);
                            }else if (material == Material.LADDER){
                                XBlock.setDirection(block,face);
                            }
                        }
                        block.getWorld().playSound(block.getLocation(), Sound.CHICKEN_EGG_POP,1,1);
                        block.getWorld().playEffect(block.getLocation(), Effect.CLOUD,1,40);
                    }
                }
                t++;
            }

            @Override
            public boolean reSchedule() {
                if (t*blocksPerTick>=blocks.length+blocksPerTick){
                    return false;
                }
                return true;
            }

            @Override
            public boolean shouldExecute() {
                if (t*blocksPerTick>=blocks.length+blocksPerTick){
                    return false;
                }
                if (System.currentTimeMillis()-timestamp<=50){
                    return false;
                }
                return true;
            }
        };
    }

}

