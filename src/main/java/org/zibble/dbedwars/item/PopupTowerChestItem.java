package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockPlaceEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.function.Acceptor;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.features.PopupTowerBuildFeature;

import java.util.LinkedHashMap;

public class PopupTowerChestItem extends BedWarsActionItem {

    public static final Key KEY = Key.of("POPUP_TOWER");

    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurablePopupTower cfgPopupTower;

    public PopupTowerChestItem(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getPopupTower().getName()),
                plugin.getConfigHandler().getCustomItems().getPopupTower().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getPopupTower().getLore()),
                XMaterial.TRAPPED_CHEST);
        this.plugin = plugin;
        this.cfgPopupTower = plugin.getConfigHandler().getCustomItems().getPopupTower();
    }

    public void onChestPlace(BlockPlaceEvent event, ArenaPlayer player) {
        event.getPlayer().sendMessage("YET TO BE FIXED FOR PAPER");
        if (!XMaterial.matchXMaterial(cfgPopupTower.getMainBlock()).isPresent()) return;
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.POPUP_TOWER_BUILD_FEATURE, PopupTowerBuildFeature.class, new Acceptor<PopupTowerBuildFeature>() {
            @Override
            public boolean accept(PopupTowerBuildFeature feature) {
                feature.build((Chest) event.getBlock().getState(), player);
                return true;
            }
        });

    }

    @Override
    public Key getKey() {
        return KEY;
    }

    public static class PopupTowerBlocks {

        public static LinkedHashMap<Block, Material> getEastBlocks(
                Block centre, XMaterial material) {
            LinkedHashMap<Block, Material> blocks = new LinkedHashMap<>();
            Block layerCentre = centre;
            blocks.put(layerCentre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 0), Material.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 1), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 0), Material.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 1), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 0), Material.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 1), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 0), Material.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 1), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(2, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 0), Material.LADDER);
            blocks.put(layerCentre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 2), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(3, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(1, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(0, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, -1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 1), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 2), material.parseMaterial());
            layerCentre = layerCentre.getRelative(0, 1, 0);
            blocks.put(layerCentre.getRelative(3, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 0), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(3, 0, 2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(2, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, -3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, -2), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-1, 0, 3), material.parseMaterial());
            blocks.put(layerCentre.getRelative(-2, 0, 2), material.parseMaterial());
            return blocks;
        }

        public static LinkedHashMap<Block, Material> getWestBlocks(
                Block centre, XMaterial material) {
            LinkedHashMap<Block, Material> blocks = new LinkedHashMap<>();

            blocks.put(centre.getRelative(-2, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, 0), Material.LADDER);
            blocks.put(centre.getRelative(-1, 0, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, 0), Material.LADDER);
            blocks.put(centre.getRelative(-1, 1, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, 0), Material.LADDER);
            blocks.put(centre.getRelative(-1, 2, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, 0), Material.LADDER);
            blocks.put(centre.getRelative(-1, 3, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 0), Material.LADDER);
            blocks.put(centre.getRelative(-1, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -3), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, 0), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, -3), material.parseMaterial());
            blocks.put(centre.getRelative(1, 6, 3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 6, -3), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, -2), material.parseMaterial());
            return blocks;
        }

        public static LinkedHashMap<Block, Material> getSouthBlocks(
                Block centre, XMaterial material) {
            LinkedHashMap<Block, Material> blocks = new LinkedHashMap<>();
            blocks.put(centre.getRelative(1, 0, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, 1), Material.LADDER);
            blocks.put(centre.getRelative(2, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, 1), Material.LADDER);
            blocks.put(centre.getRelative(2, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 1, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, 1), Material.LADDER);
            blocks.put(centre.getRelative(2, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 2, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, 1), Material.LADDER);
            blocks.put(centre.getRelative(2, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 1), Material.LADDER);
            blocks.put(centre.getRelative(2, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 6, 3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 6, -2), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, 3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, 3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 6, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, -2), material.parseMaterial());
            return blocks;
        }

        public static LinkedHashMap<Block, Material> getNorthBlocks(
                Block centre, XMaterial material) {
            LinkedHashMap<Block, Material> blocks = new LinkedHashMap<>();
            blocks.put(centre.getRelative(1, 0, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 0, -1), Material.LADDER);
            blocks.put(centre.getRelative(2, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 0, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 0, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 0, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 1, -1), Material.LADDER);
            blocks.put(centre.getRelative(2, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 1, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 1, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 1, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 2, -1), Material.LADDER);
            blocks.put(centre.getRelative(2, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 2, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 2, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 2, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, -1), Material.LADDER);
            blocks.put(centre.getRelative(2, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 3, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 3, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, -1), Material.LADDER);
            blocks.put(centre.getRelative(2, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 0), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(0, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, -3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, -3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, -2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 4, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 4, 2), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, -1), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 0), material.parseMaterial());
            blocks.put(centre.getRelative(1, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-1, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, -3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, -2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 5, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 5, 2), material.parseMaterial());
            blocks.put(centre.getRelative(0, 6, -3), material.parseMaterial());
            blocks.put(centre.getRelative(0, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, -3), material.parseMaterial());
            blocks.put(centre.getRelative(3, 6, -2), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, -3), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, -2), material.parseMaterial());
            blocks.put(centre.getRelative(3, 6, 1), material.parseMaterial());
            blocks.put(centre.getRelative(2, 6, 2), material.parseMaterial());
            blocks.put(centre.getRelative(-3, 6, 1), material.parseMaterial());
            blocks.put(centre.getRelative(-2, 6, 2), material.parseMaterial());
            return blocks;
        }

    }

}
