package com.pepedevs.dbedwars.item;

import com.pepedevs.corelib.particles.ParticleBuilder;
import com.pepedevs.corelib.particles.ParticleEffect;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.corelib.utils.xseries.XSound;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.task.PopupTowerWorkload;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PopupTowerChestItem extends PluginActionItem {

    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurablePopupTower cfgPopupTower;

    public PopupTowerChestItem(DBedwars plugin) {
        super(plugin.configTranslator().translate(plugin.getConfigHandler().getCustomItems().getPopupTower().getName()),
                plugin.configTranslator().translate(plugin.getConfigHandler().getCustomItems().getPopupTower().getLore() == null ? new ArrayList<>()
                        : plugin.getConfigHandler().getCustomItems().getPopupTower().getLore()),
                XMaterial.TRAPPED_CHEST.parseMaterial());
        this.plugin = plugin;
        this.cfgPopupTower = plugin.getConfigHandler().getCustomItems().getPopupTower();
    }

    public void onChestPlace(BlockPlaceEvent event, ArenaPlayer player) {
        event.getPlayer().sendMessage("YET TO BE FIXED FOR PAPER");
        if (!XMaterial.matchXMaterial(cfgPopupTower.getMainBlock()).isPresent()) return;

        plugin.getThreadHandler()
                .submitSync(() -> event.getBlock().setType(XMaterial.AIR.parseMaterial()));

        plugin.getThreadHandler()
                .submitAsync(
                        new PopupTowerWorkload(
                                XMaterial.matchXMaterial(cfgPopupTower.getMainBlock()).get(),
                                (cfgPopupTower.getSound() == null
                                        ? new SoundVP(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                                        : cfgPopupTower.getSound().equals("")
                                                ? new SoundVP(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                                                : SoundVP.valueOf(cfgPopupTower.getSound())),
                                new ParticleBuilder(ParticleEffect.CLOUD)
                                        .setAmount(1)
                                        .setSpeed(0.2F),
                                event.getBlockPlaced(),
                                player,
                                2));
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
