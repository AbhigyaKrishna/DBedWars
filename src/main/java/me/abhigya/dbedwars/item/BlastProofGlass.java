package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlastProofGlass extends PluginActionItem {

    private final DBedwars plugin;

    public BlastProofGlass(DBedwars plugin) {
        super(
                plugin,
                plugin.getConfigHandler().getCustomItems().getBlastProofGlass().getGlassItemName(),
                plugin.getConfigHandler().getCustomItems().getBlastProofGlass().getGlassItemLore(),
                XMaterial.GLASS.parseMaterial());
        this.plugin = plugin;
    }

    public void onPlace(BlockPlaceEvent event) {
        plugin.getNMSAdaptor().setBlockResistance(event.getBlock(), Float.MAX_VALUE / 5);
    }

    public void onTNTExplode(EntityExplodeEvent event) {
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.getType() != Material.AIR) {
                if (Utils.containsGlass(
                                getBlockPathX(
                                        event.getEntity().getLocation().getBlock().getLocation(),
                                        block.getLocation()))
                        && Utils.containsGlass(
                                getBlockPathY(
                                        event.getEntity().getLocation().getBlock().getLocation(),
                                        block.getLocation()))) {
                    blockIterator.remove();
                }
            }
        }
    }

    public void onFireballExplode(EntityExplodeEvent event) {
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (XMaterial.matchXMaterial(block.getType()) == XMaterial.END_STONE) {
                blockIterator.remove();
            }
            if (block.getType() != Material.AIR
                    && XMaterial.matchXMaterial(block.getType()) != XMaterial.END_STONE) {
                if (Utils.containsGlassOrEndstone(
                                getBlockPathX(
                                        event.getEntity().getLocation().getBlock().getLocation(),
                                        block.getLocation()))
                        && Utils.containsGlassOrEndstone(
                                getBlockPathY(
                                        event.getEntity().getLocation().getBlock().getLocation(),
                                        block.getLocation()))) {
                    blockIterator.remove();
                }
            }
        }
    }

    private List<Material> getBlockPathX(Location startLoc, Location endLoc) {
        List<Block> blocks = new ArrayList<>();
        Location currentLoc = startLoc.clone();
        Location endLocation = endLoc.clone();

        for (int i = 0; i < 30; i++) {
            double xDiff = endLocation.getX() - currentLoc.getX();
            double modXDiff = xDiff;
            if (xDiff < 0) {
                modXDiff = xDiff * (-1);
            }
            double yDiff = endLocation.getY() - currentLoc.getY();
            double modYDiff = yDiff;
            if (yDiff < 0) {
                modYDiff = yDiff * (-1);
            }
            double zDiff = endLocation.getZ() - currentLoc.getZ();
            double modZDiff = zDiff;
            if (zDiff < 0) {
                modZDiff = zDiff * (-1);
            }

            if (modXDiff >= modYDiff && modXDiff >= modZDiff) {
                currentLoc.add(xDiff / modXDiff, 0, 0);
                blocks.add(currentLoc.getBlock());
            } else if (modZDiff >= modXDiff && modZDiff >= modYDiff) {
                currentLoc.add(0, 0, zDiff / modZDiff);
                blocks.add(currentLoc.getBlock());
            } else {
                currentLoc.add(0, yDiff / modYDiff, 0);
                blocks.add(currentLoc.getBlock());
            }
            if (blocks.contains(endLocation.getBlock())) {
                break;
            }
        }

        return getMaterials(blocks);
    }

    private List<Material> getBlockPathY(Location startLoc, Location endLoc) {
        List<Block> blocks = new ArrayList<>();
        Location currentLoc = startLoc.clone();
        Location endLocation = endLoc.clone();

        for (int i = 0; i < 25; i++) {
            double xDiff = endLocation.getX() - currentLoc.getX();
            double modXDiff = xDiff;
            if (xDiff < 0) {
                modXDiff = xDiff * (-1);
            }
            double yDiff = endLocation.getY() - currentLoc.getY();
            double modYDiff = yDiff;
            if (yDiff < 0) {
                modYDiff = yDiff * (-1);
            }
            double zDiff = endLocation.getZ() - currentLoc.getZ();
            double modZDiff = zDiff;
            if (zDiff < 0) {
                modZDiff = zDiff * (-1);
            }

            if (modYDiff >= modXDiff && modYDiff >= modZDiff) {
                currentLoc.add(0, 0, zDiff / modZDiff);
                blocks.add(currentLoc.getBlock());
            } else if (modXDiff >= modYDiff && modXDiff >= modZDiff) {
                currentLoc.add(xDiff / modXDiff, 0, 0);
                blocks.add(currentLoc.getBlock());
            } else {
                currentLoc.add(0, yDiff / modYDiff, 0);
                blocks.add(currentLoc.getBlock());
            }
            if (blocks.contains(endLocation.getBlock())) {
                break;
            }
        }

        return getMaterials(blocks);
    }

    private List<Material> getBlockPathZ(Location startLoc, Location endLoc) {
        List<Block> blocks = new ArrayList<>();
        Location currentLoc = startLoc.clone();
        Location endLocation = endLoc.clone();

        for (int i = 0; i < 25; i++) {
            double xDiff = endLocation.getX() - currentLoc.getX();
            double modXDiff = xDiff;
            if (xDiff < 0) {
                modXDiff = xDiff * (-1);
            }
            double yDiff = endLocation.getY() - currentLoc.getY();
            double modYDiff = yDiff;
            if (yDiff < 0) {
                modYDiff = yDiff * (-1);
            }
            double zDiff = endLocation.getZ() - currentLoc.getZ();
            double modZDiff = zDiff;
            if (zDiff < 0) {
                modZDiff = zDiff * (-1);
            }

            if (modZDiff >= modXDiff && modZDiff >= modYDiff) {
                currentLoc.add(0, 0, zDiff / modZDiff);
                blocks.add(currentLoc.getBlock());
            } else if (modXDiff >= modYDiff && modXDiff >= modZDiff) {
                currentLoc.add(xDiff / modXDiff, 0, 0);
                blocks.add(currentLoc.getBlock());
            } else {
                currentLoc.add(0, yDiff / modYDiff, 0);
                blocks.add(currentLoc.getBlock());
            }
            if (blocks.contains(endLocation.getBlock())) {
                break;
            }
        }

        return getMaterials(blocks);
    }

    @NotNull
    private List<Material> getMaterials(List<Block> blocks) {
        List<Material> materials = new ArrayList<>();

        blocks.removeIf(
                block ->
                        !(block.getType().name().contains("GLASS")
                                || XMaterial.matchXMaterial(block.getType())
                                        == XMaterial.END_STONE));
        blocks.forEach(
                block -> {
                    if (!materials.contains(block.getType())) {
                        materials.add(block.getType());
                    }
                });

        return materials;
    }
}
