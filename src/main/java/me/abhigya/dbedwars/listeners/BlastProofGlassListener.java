package me.abhigya.dbedwars.listeners;

import me.Abhigya.core.util.reflection.general.FieldReflection;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlastProofGlassListener implements Listener {

    //TODO ADD A METADATA CHECK AND NBT CHECK

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.getBlock().getType().name().contains("GLASS")) {
            net.minecraft.server.v1_8_R3.Block block = CraftMagicNumbers.getBlock(event.getBlock());
            FieldReflection.getAccessible(net.minecraft.server.v1_8_R3.Block.class, "durability").set(block, Float.MAX_VALUE / 5);
        }
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent event){
        if (event.getEntityType() != EntityType.PRIMED_TNT)
            return;
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.getType() != Material.AIR) {
                if (Utils.containsGlass(getBlockPaths1(event.getEntity().getLocation().getBlock().getLocation(),block.getLocation())) &&
                        Utils.containsGlass(getBlockPaths2(event.getEntity().getLocation().getBlock().getLocation(),block.getLocation()))){
                    blockIterator.remove();
                }
            }
        }
    }

    @EventHandler
    public void onFireBallExplode(EntityExplodeEvent event){
        if (event.getEntityType() != EntityType.FIREBALL)
            return;
        Iterator<Block> blockIterator = event.blockList().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (XMaterial.matchXMaterial(block.getType()) == XMaterial.END_STONE){
                blockIterator.remove();
            }
            if (block.getType() != Material.AIR && XMaterial.matchXMaterial(block.getType()) != XMaterial.END_STONE) {
                if (Utils.containsGlassOrEndstone(getBlockPaths1(event.getEntity().getLocation().getBlock().getLocation(),block.getLocation())) &&
                        Utils.containsGlassOrEndstone(getBlockPaths2(event.getEntity().getLocation().getBlock().getLocation(),block.getLocation()))){
                    blockIterator.remove();
                }
            }
        }
    }

    private List<Material> getBlockPaths1(Location startLoc, Location endLoc) {
        List<Block> blocks = new ArrayList<>();
        Location currentLoc = startLoc.clone();
        Location endLocation = endLoc.clone();

        for (int i = 0; i < 30; i++) {
            double xDiff = endLocation.getX()-currentLoc.getX();
            double modXDiff = xDiff;
            if(xDiff<0){
                modXDiff = xDiff*(-1);
            }
            double yDiff = endLocation.getY()-currentLoc.getY();
            double modYDiff = yDiff;
            if (yDiff<0){
                modYDiff = yDiff*(-1);
            }
            double zDiff = endLocation.getZ()-currentLoc.getZ();
            double modZDiff = zDiff;
            if(zDiff<0){
                modZDiff = zDiff*(-1);
            }

            if (modXDiff>=modYDiff && modXDiff>=modZDiff){
                currentLoc.add(xDiff/modXDiff,0,0);
                blocks.add(currentLoc.getBlock());
            }else if (modZDiff>=modXDiff && modZDiff>=modYDiff){
                currentLoc.add(0,0,zDiff/modZDiff);
                blocks.add(currentLoc.getBlock());
            }else {
                currentLoc.add(0,yDiff/modYDiff,0);
                blocks.add(currentLoc.getBlock());
            }
            if (blocks.contains(endLocation.getBlock())){
                break;
            }
        }

        List<Material> materials = new ArrayList<>();
        blocks.removeIf(block -> !(block.getType().name().contains("GLASS") || XMaterial.matchXMaterial(block.getType()) == XMaterial.END_STONE));

        blocks.forEach(block -> {
            if (!materials.contains(block.getType())){
                materials.add(block.getType());
            }
        });

        return materials;
    }

    private List<Material> getBlockPaths2(Location startLoc, Location endLoc) {
        List<Block> blocks = new ArrayList<>();
        Location currentLoc = startLoc.clone();
        Location endLocation = endLoc.clone();

        for (int i = 0; i < 25; i++) {
            double xDiff = endLocation.getX()-currentLoc.getX();
            double modXDiff = xDiff;
            if(xDiff<0){
                modXDiff = xDiff*(-1);
            }
            double yDiff = endLocation.getY()-currentLoc.getY();
            double modYDiff = yDiff;
            if (yDiff<0){
                modYDiff = yDiff*(-1);
            }
            double zDiff = endLocation.getZ()-currentLoc.getZ();
            double modZDiff = zDiff;
            if(zDiff<0){
                modZDiff = zDiff*(-1);
            }

            if (modZDiff>=modXDiff && modZDiff>=modYDiff){
                currentLoc.add(0,0,zDiff/modZDiff);
                blocks.add(currentLoc.getBlock());
            } else if(modXDiff>=modYDiff && modXDiff>=modZDiff){
                currentLoc.add(xDiff/modXDiff,0,0);
                blocks.add(currentLoc.getBlock());
            }else {
                currentLoc.add(0,yDiff/modYDiff,0);
                blocks.add(currentLoc.getBlock());
            }
            if (blocks.contains(endLocation.getBlock())){
                break;
            }
        }

        List<Material> materials = new ArrayList<>();

        blocks.removeIf(block -> !(block.getType().name().contains("GLASS") || XMaterial.matchXMaterial(block.getType()) == XMaterial.END_STONE));
        blocks.forEach(block -> {
            if (!materials.contains(block.getType())){
                materials.add(block.getType());
            }
        });

        return materials;
    }

}
