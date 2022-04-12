package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class BlastProofGlass extends BedWarsActionItem {

    public static final Key KEY = Key.of("BLAST_PROOF_GLASS");
    private static final Predicate<Material> GLASS_PREDICATE = material -> !material.name().contains("GLASS");
    private static final Predicate<Material> GLASS_OR_ENDSTONE_PREDICATE = material -> GLASS_PREDICATE.test(material) || XMaterial.END_STONE.parseMaterial().equals(material);
    private final DBedwars plugin;

    public BlastProofGlass(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getBlastProofGlass().getName()),
                ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getBlastProofGlass().getLore()),
                XMaterial.GLASS);
        this.plugin = plugin;
    }

    @Override
    public void give(Player player) {
        // TODO Get ArenaPlayer then set type
        this.plugin.getGameManager().getArenaPlayer(player).ifPresent(ap -> super.give(player));
    }

    public void onPlace(BlockPlaceEvent event) {
        this.plugin.getNMSAdaptor().setBlockResistance(event.getBlock(), Float.MAX_VALUE / 5);
    }

    public void onTNTExplode(EntityExplodeEvent event) {
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.getType() != Material.AIR) {
                if (Util.anyMatch(getBlockPathX(event.getEntity().getLocation().getBlock().getLocation(), block.getLocation()), GLASS_PREDICATE)
                        && Util.anyMatch(getBlockPathY(event.getEntity().getLocation().getBlock().getLocation(), block.getLocation()), GLASS_PREDICATE)) {
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
            if (block.getType() != Material.AIR && XMaterial.matchXMaterial(block.getType()) != XMaterial.END_STONE) {
                if (Util.anyMatch(getBlockPathX(event.getEntity().getLocation().getBlock().getLocation(), block.getLocation()), GLASS_OR_ENDSTONE_PREDICATE)
                        && Util.anyMatch(getBlockPathY(event.getEntity().getLocation().getBlock().getLocation(), block.getLocation()), GLASS_OR_ENDSTONE_PREDICATE)) {
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

        blocks.removeIf(block -> !(block.getType().name().contains("GLASS")
                || XMaterial.matchXMaterial(block.getType()) == XMaterial.END_STONE));
        blocks.forEach(block -> {
            if (!materials.contains(block.getType())) {
                materials.add(block.getType());
            }
        });

        return materials;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

}
