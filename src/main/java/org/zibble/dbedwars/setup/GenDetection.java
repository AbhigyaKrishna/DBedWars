package org.zibble.dbedwars.setup;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XTag;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class GenDetection {

    private GenDetection(){}
    private static final BlockFace[] SQUARE_CHECK;

    private static final EnumMap<BlockFace, BlockFace[]> RECTANGLE_CHECK = new EnumMap<>(BlockFace.class);

    public static Optional<Block> detect(Location location, int radius) {
        int x = location.getBlockX();
        int y = location.getBlockY() - 1;
        int z = location.getBlockZ();

        List<Block> blocks = new ArrayList<>();

        for (int i = x-radius; i <= x+radius ; i++) {
            for (int k = z-radius; k <= z+radius; k++) {
                Block block = location.getWorld().getBlockAt(i, y, k);
                //Bottom slab check
                XMaterial type = XMaterial.matchXMaterial(block.getType());
                if (!(XTag.WOODEN_SLABS.isTagged(type) || XTag.NON_WOODEN_SLABS.isTagged(type))) continue;
                    blocks.add(block);
            }
        }

        Block squareCheck = checkSquare(blocks);
        if (squareCheck == null) {
            Block rectangleCheck = checkRectangle(blocks);
            if (rectangleCheck == null) return Optional.empty();
            return Optional.of(rectangleCheck);
        }
        return Optional.of(squareCheck);

    }

    private static Block checkSquare(List<Block> blocks) {
        for (Block block : blocks) {
            boolean isCentre = true;
            for (BlockFace face : SQUARE_CHECK) {
                isCentre = isCentre && blocks.contains(block.getRelative(face));
            }
            if (isCentre) return block;
        }
        return null;
    }

    private static Block checkRectangle(List<Block> blocks) {
        for (Block block : blocks) {
            for (Map.Entry<BlockFace, BlockFace[]> entry : RECTANGLE_CHECK.entrySet()) {
                boolean isCentre = true;
                for (BlockFace face : entry.getValue()) {
                    isCentre = isCentre && blocks.contains(block.getRelative(face));
                }
                if (isCentre) return block;
            }
        }
        return null;
    }

    static {
        SQUARE_CHECK = new BlockFace[]{
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.EAST,
                BlockFace.WEST,
                BlockFace.NORTH_EAST,
                BlockFace.NORTH_WEST,
                BlockFace.SOUTH_EAST,
                BlockFace.SOUTH_WEST
        };
        RECTANGLE_CHECK.put(BlockFace.NORTH, new BlockFace[]{
                BlockFace.SOUTH,
                BlockFace.WEST,
                BlockFace.EAST,
                BlockFace.SOUTH_WEST,
                BlockFace.SOUTH_EAST
        });
        RECTANGLE_CHECK.put(BlockFace.SOUTH, new BlockFace[]{
                BlockFace.NORTH,
                BlockFace.WEST,
                BlockFace.EAST,
                BlockFace.NORTH_WEST,
                BlockFace.NORTH_EAST
        });
        RECTANGLE_CHECK.put(BlockFace.EAST, new BlockFace[]{
                BlockFace.WEST,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.NORTH_WEST,
                BlockFace.SOUTH_WEST
        });
        RECTANGLE_CHECK.put(BlockFace.WEST, new BlockFace[]{
                BlockFace.EAST,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.NORTH_EAST,
                BlockFace.SOUTH_EAST
        });
    }

}
