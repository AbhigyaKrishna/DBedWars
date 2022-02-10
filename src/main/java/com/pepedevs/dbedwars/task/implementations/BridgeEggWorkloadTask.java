package com.pepedevs.dbedwars.task.implementations;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.api.task.Workload;
import com.pepedevs.radium.utils.xseries.XBlock;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;

public class BridgeEggWorkloadTask implements Workload {

    private final DBedwars plugin;
    private final Egg egg;
    private final BlockFace[] faces;
    private final ConfigurableCustomItems.ConfigurableBridgeEgg cfg;
    private final ArenaPlayer arenaPlayer;
    private int tick;
    private long timestamp;
    private Block lastBlock;

    public BridgeEggWorkloadTask(
            DBedwars plugin,
            ArenaPlayer arenaPlayer,
            Egg egg,
            ConfigurableCustomItems.ConfigurableBridgeEgg cfg) {
        this.plugin = plugin;
        this.cfg = cfg;
        this.arenaPlayer = arenaPlayer;
        tick = 0;
        this.egg = egg;
        this.lastBlock = null;
        faces = this.getDirection(arenaPlayer.getPlayer(), cfg.isFlipBridgeEnabled());
    }

    @Override
    public void compute() {
        timestamp = System.currentTimeMillis();
        tick++;
        Location eggLocation = egg.getLocation().clone();
        Block block = eggLocation.subtract(0, 2, 0).getBlock();
        DyeColor color = arenaPlayer.getTeam().getColor().getDyeColor();
        placeBlock(block, color);
        placeBlock(block.getRelative(faces[0]), color);
        placeBlock(block.getRelative(faces[1]), color);
    }

    @Override
    public boolean reSchedule() {
        if (egg.isDead()) {
            return false;
        }
        if (tick >= cfg.getKeepAliveTimeOut()) {
            return false;
        }
        if (this.arenaPlayer.getPlayer().getLocation().distance(egg.getLocation()) >= cfg.getMaxDistanceFromPlayer()) {
            return false;
        }
        return !(this.arenaPlayer.getPlayer().getLocation().getY() - egg.getLocation().getY() >= cfg.getMaxDownStack());
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp <= 5) {
            return false;
        }

        if (egg.isDead()) {
            return false;
        }
        if (tick >= cfg.getKeepAliveTimeOut()) {
            return false;
        }
        if (this.arenaPlayer.getPlayer().getLocation().distance(egg.getLocation()) <= cfg.getMinDistanceFromPlayer()) {
            return false;
        }
        if (this.arenaPlayer.getPlayer().getLocation().distance(egg.getLocation()) >= cfg.getMaxDistanceFromPlayer()) {
            return false;
        }
        return !(this.arenaPlayer.getPlayer().getLocation().getY() - egg.getLocation().getY() >= cfg.getMaxDownStack());
    }

    private void placeBlock(Block block, DyeColor dyeColor) {
        if (block.getType() == Material.AIR) {
            plugin.getThreadHandler().submitSync(new Runnable() {
                @Override
                public void run() {
                    BridgeEggWorkloadTask.this.arenaPlayer.getArena().setBlock(block, Material.WOOL);
                    block.getWorld().playSound(block.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
                    XBlock.setColor(block, dyeColor);
                }
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
            DyeColor color = arenaPlayer.getTeam().getColor().getDyeColor();
            placeBlock(block, color);
            placeBlock(block.getRelative(faces[0]), color);
            placeBlock(block.getRelative(faces[1]), color);
        }
    }
}
