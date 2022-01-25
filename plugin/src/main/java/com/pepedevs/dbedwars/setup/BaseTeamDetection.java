package com.pepedevs.dbedwars.setup;

import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.radium.utils.xseries.XBlock;
import com.pepedevs.radium.utils.xseries.XMaterial;
import com.pepedevs.radium.utils.xseries.XTag;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseTeamDetection {

    private static final Map<XTag<XMaterial>, Integer> WEIGHTS;

    public static Optional<Color> detect(Location location, int radius) {
        Map<Color, Integer> teamWeights = new HashMap<>();
        int x = location.getBlockX();
        int z = location.getBlockZ();
        for (int i = x-radius; i <= x+radius; i++) {
            for (int k = 0; k <= z+radius; k++) {
                for (int j = 0; j < 256 ; j++) {
                    Block block = location.getWorld().getBlockAt(i, j ,k);
                    int weight = getWeight(block.getType());
                    if (weight == -1) continue;
                    DyeColor blockColor = XBlock.getColor(block);
                    if (blockColor == null) continue;
                    Optional<Color> optionalColor = Color.from(blockColor);
                    if (!optionalColor.isPresent()) continue;
                    Color color = optionalColor.get();
                    Integer oldVal = teamWeights.get(color);
                    int newVal = (oldVal == null ? 0 : oldVal) + weight;
                    teamWeights.put(color, newVal);
                }
            }
        }
        return Optional.ofNullable(getHighest(teamWeights));
    }

    private static int getWeight(Material material) {
        XMaterial xMaterial = XMaterial.matchXMaterial(material);
        for (Map.Entry<XTag<XMaterial>, Integer> entry : WEIGHTS.entrySet()) {
            if (entry.getKey().isTagged(xMaterial)) return entry.getValue();
        }
        return -1;
    }

    private static Color getHighest(Map<Color, Integer> map){
        Map.Entry<Color, Integer> highest = null;
        for (Map.Entry<Color, Integer> entry : map.entrySet()) {
            if (highest == null || entry.getValue() > highest.getValue()) highest = entry;
        }
        return (highest == null ? null : highest.getKey());
    }

    static {
        WEIGHTS = new HashMap<>();
        WEIGHTS.put(XTag.CONCRETE, 10);
        WEIGHTS.put(XTag.WOOL, 10);
        WEIGHTS.put(XTag.CONCRETE_POWDER, 10);
        WEIGHTS.put(XTag.CARPETS, 3);
        WEIGHTS.put(XTag.TERRACOTTA, 10);
        WEIGHTS.put(XTag.GLAZED_TERRACOTTA, 3);
        WEIGHTS.put(XTag.GLASS, 5);
        WEIGHTS.put(XTag.BANNERS, 5);
    }

}
