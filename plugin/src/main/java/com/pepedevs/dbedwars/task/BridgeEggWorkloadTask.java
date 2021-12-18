package com.pepedevs.dbedwars.task;

import com.pepedevs.corelib.task.Workload;
import com.pepedevs.corelib.utils.xseries.XBlock;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class BridgeEggWorkloadTask implements Workload {

    private final DBedwars plugin;
    private final int keepAliveTimeout;
    private final double maxEggDistance;
    private final Player player;
    private final Projectile egg;
    private final double maxDownStack;
    private final double minDistanceToStartPlacingBlocks;
    private final DyeColor dyeColor;
    private final BlockFace[] faces;
    private final Arena arena;
    private int tick;
    private long timestamp;
    private Block lastBlock;

    public BridgeEggWorkloadTask(
            DBedwars plugin,
            Arena arena,
            Player player,
            DyeColor dyeColor,
            Projectile egg,
            double minDistanceToPlaceBlocks,
            int ticksToKeepEggAliveFor,
            double distanceToKeepEggAliveFor,
            double maxDownStack,
            boolean flipBridge) {
        this.plugin = plugin;
        this.arena = arena;
        tick = 0;
        keepAliveTimeout = ticksToKeepEggAliveFor;
        maxEggDistance = distanceToKeepEggAliveFor;
        this.player = player;
        this.egg = egg;
        this.maxDownStack = maxDownStack;
        this.minDistanceToStartPlacingBlocks = minDistanceToPlaceBlocks;
        this.dyeColor = dyeColor;
        this.lastBlock = null;
        faces = getDirection(player, flipBridge);
    }

    @Override
    public void compute() {
        timestamp = System.currentTimeMillis();
        tick++;
        Location eggLocation = egg.getLocation().clone();
        Block block = eggLocation.subtract(0, 2, 0).getBlock();
        placeBlock(block, dyeColor);
        placeBlock(block.getRelative(faces[0]), dyeColor);
        placeBlock(block.getRelative(faces[1]), dyeColor);
    }

    @Override
    public boolean reSchedule() {
        if (egg.isDead()) {
            return false;
        }
        if (tick >= keepAliveTimeout) {
            return false;
        }
        if (player.getLocation().distance(egg.getLocation()) >= maxEggDistance) {
            return false;
        }
        return !(player.getLocation().getY() - egg.getLocation().getY() >= maxDownStack);
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp <= 5) {
            return false;
        }

        if (egg.isDead()) {
            return false;
        }
        if (tick >= keepAliveTimeout) {
            return false;
        }
        if (player.getLocation().distance(egg.getLocation()) <= minDistanceToStartPlacingBlocks) {
            return false;
        }
        if (player.getLocation().distance(egg.getLocation()) >= maxEggDistance) {
            return false;
        }
        return !(player.getLocation().getY() - egg.getLocation().getY() >= maxDownStack);
    }

    private void placeBlock(Block block, DyeColor dyeColor) {
        if (block.getType() == Material.AIR) {
            plugin.getThreadHandler()
                    .submitSync(
                            () -> {
                                arena.setBlock(block, Material.WOOL);
                                block.getWorld()
                                        .playSound(
                                                block.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
                                XBlock.setColor(block, dyeColor);
                            });
        }
    }

    private BlockFace[] getDirection(Player player, boolean flip) {
        float degree;
        BlockFace[] faces = new BlockFace[2];
        if (player.getLocation().getYaw() < 0) {
            degree = player.getLocation().getYaw() + 360;
        } else {
            degree = player.getLocation().getYaw();
        }
        if (flip) {
            degree += 180;
            if (degree > 360) {
                degree -= 360;
            }
        }
        if (degree >= 0 && degree <= 90) {
            faces[0] = BlockFace.NORTH;
            faces[1] = BlockFace.EAST;
        } else if (degree >= 90 && degree <= 180) {
            faces[0] = BlockFace.SOUTH;
            faces[1] = BlockFace.EAST;
        } else if (degree >= 180 && degree <= 270) {
            faces[0] = BlockFace.SOUTH;
            faces[1] = BlockFace.WEST;
        } else {
            faces[0] = BlockFace.NORTH;
            faces[1] = BlockFace.EAST;
        }
        for (int i = 0; i < faces.length; i++) {
            faces[i] = faces[i].getOppositeFace();
        }
        return faces;
    }

    private void skipBlockFill(Block block) {
        if (this.lastBlock != null) {
            int xDiff = block.getX() - this.lastBlock.getX();
            int zDiff = block.getZ() - this.lastBlock.getZ();
            int yDiff = block.getY() - this.lastBlock.getY();
            if (xDiff >= 2) {
                Block temp = this.lastBlock.getLocation().add(1, 0, 0).getBlock();
                placeEggBlock(temp);
                this.lastBlock = block;
            } else if (xDiff <= -2) {
                Block temp = this.lastBlock.getLocation().add(-1, 0, 0).getBlock();
                placeEggBlock(temp);
                this.lastBlock = block;
            }
            if (zDiff >= 2.0) {
                Block temp = this.lastBlock.getLocation().add(0, 0, 1).getBlock();
                placeEggBlock(temp);
                this.lastBlock = block;
            } else if (zDiff <= -2.0) {
                Block temp = this.lastBlock.getLocation().add(0, 0, -1).getBlock();
                placeEggBlock(temp);
                this.lastBlock = block;
            }
            if (yDiff > 1.1) {
                Block temp = this.lastBlock.getLocation().add(0, 1, 0).getBlock();
                placeEggBlock(temp);
                this.lastBlock = block;
            }
            if (yDiff < -1.1) {
                Block temp = this.lastBlock.getLocation().add(0, -1, 0).getBlock();
                placeEggBlock(temp);
            }
        }
        this.lastBlock = block;
    }

    public void placeEggBlock(Block block) {
        skipBlockFill(block);
        if (block.getType() == Material.AIR) {
            placeBlock(block, dyeColor);
            placeBlock(block.getRelative(faces[0]), dyeColor);
            placeBlock(block.getRelative(faces[1]), dyeColor);
        }
    }

}
