package com.pepedevs.dbedwars.item;

import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum InnerCustomItem {
    SIMPLE_SETUP_ITEM(
            new PluginActionItem(
                    StringUtils.translateAlternateColorCodes("&bSimple Arena Setup"),
                    StringUtils.translateAlternateColorCodes(
                            new ArrayList<>(
                                    Collections.singletonList(
                                            "&7Right click to view arena settings panel."))),
                    XMaterial.NETHER_STAR.parseMaterial()) {
                @Override
                public void onActionPerform(
                        Player player,
                        EnumAction enumAction,
                        PlayerInteractEvent playerInteractEvent) {
                    Map<String, Object> info = new HashMap<>();
                    info.put(
                            "arena",
                            DBedwars.getInstance()
                                    .getGameManager()
                                    .getArena(
                                            NBTUtils.getValue(
                                                    playerInteractEvent.getItem(),
                                                    "arena",
                                                    String.class)));
                    info.put("page", 1);
                    if (enumAction == EnumAction.RIGHT_CLICK
                            || enumAction == EnumAction.RIGHT_CLICK_SNEAKING
                            || enumAction == EnumAction.RIGHT_CLICK_SPRINTING)
                        DBedwars.getInstance()
                                .getGuiHandler()
                                .getGuis()
                                .get("SIMPLE_SETUP")
                                .open(null, info, player);
                }
            }),

    ADVANCED_SETUP_ITEM(
            new PluginActionItem(
                    StringUtils.translateAlternateColorCodes("&bAdvanced Arena Setup"),
                    StringUtils.translateAlternateColorCodes(
                            new ArrayList<>(
                                    Collections.singletonList(
                                            "&7Right click to view arena settings panel."))),
                    XMaterial.NETHER_STAR.parseMaterial()) {
                @Override
                public void onActionPerform(
                        Player player,
                        EnumAction enumAction,
                        PlayerInteractEvent playerInteractEvent) {
                    Map<String, Object> info = new HashMap<>();
                    info.put(
                            "arena",
                            DBedwars.getInstance()
                                    .getGameManager()
                                    .getArena(
                                            NBTUtils.getValue(
                                                    playerInteractEvent.getItem(),
                                                    "arena",
                                                    String.class)));
                    info.put("page", 1);
                    if (enumAction == EnumAction.RIGHT_CLICK
                            || enumAction == EnumAction.RIGHT_CLICK_SNEAKING
                            || enumAction == EnumAction.RIGHT_CLICK_SPRINTING)
                        DBedwars.getInstance()
                                .getGuiHandler()
                                .getGuis()
                                .get("ADVANCED_SETUP")
                                .open(null, info, player);
                }
            }),
    ;

    private final PluginActionItem item;

    InnerCustomItem(PluginActionItem item) {
        this.item = item;
    }

    public PluginActionItem getItem() {
        return item;
    }
}
