package org.zibble.dbedwars.game.setup.detection;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.zibble.dbedwars.api.objects.math.BoundingBox;

import java.util.Optional;
import java.util.Set;

public class BedDetection {

    private BedDetection() {
    }

    public static Optional<Block> detect(Location location, int radius) {
        BoundingBox box = new BoundingBox(location.clone().subtract(radius, 0, radius).toVector(), location.clone().add(radius, 0, radius).toVector());
        Optional<Block> block = checkBed(box.getBlocks(location.getWorld()));
        if (block.isPresent()) {
            return block;
        }

        box = new BoundingBox(location.clone().subtract(radius, -1, radius).toVector(), location.clone().add(radius, radius, radius).toVector());
        block = checkBed(box.getBlocks(location.getWorld()));
        if (block.isPresent()) {
            return block;
        }

        box = new BoundingBox(location.clone().subtract(radius, radius, radius).toVector(), location.clone().add(radius, -1, radius).toVector());
        block = checkBed(box.getBlocks(location.getWorld()));
        return block;
    }

    private static Optional<Block> checkBed(Set<Block> blocks) {
        for (Block block : blocks) {
            if (block.getType().name().contains("BED")) {
                return Optional.of(block);
            }
        }
        return Optional.empty();
    }

}
