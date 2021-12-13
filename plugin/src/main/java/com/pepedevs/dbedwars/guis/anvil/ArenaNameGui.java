package com.pepedevs.dbedwars.guis.anvil;

import com.pepedevs.corelib.menu.anvil.AnvilItem;
import com.pepedevs.corelib.menu.anvil.AnvilMenu;
import com.pepedevs.corelib.menu.anvil.action.AnvilItemClickAction;
import com.pepedevs.corelib.menu.inventory.action.ItemClickAction;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.util.gui.IAnvilMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ArenaNameGui extends IAnvilMenu {

    private final DBedwars plugin;

    public ArenaNameGui(DBedwars plugin) {
        super(
                "ARENA_NAME_SETUP",
                new AnvilMenu(
                        new AnvilItem(
                                StringUtils.translateAlternateColorCodes("Enter name"),
                                XMaterial.PAPER.parseItem()) {
                            @Override
                            public void onClick(AnvilItemClickAction anvilItemClickAction) {}
                        },
                        null));

        this.plugin = plugin;

        this.menu.setOutputAction(
                action -> {
                    if (action.getClickedItem().getItemMeta() != null
                            && action.getClickedItem().getItemMeta().hasDisplayName()) {
                        String name = action.getClickedItem().getItemMeta().getDisplayName();

                        if (ArenaNameGui.this.plugin.getGameManager().containsArena(name)) {
                            action.getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&cArena with this name already exist!"));
                        } else {
                            Arena arena =
                                    ArenaNameGui.this.plugin.getGameManager().createArena(name);
                            arena.saveData(false);

                            Map<String, Object> info = new HashMap<>();
                            info.put("arena", arena);
                            info.put("type", "initial");

                            ArenaNameGui.this.menu.close(action.getPlayer());
                            ArenaNameGui.this
                                    .plugin
                                    .getGuiHandler()
                                    .getGui("MAP_SETUP")
                                    .open(null, info, action.getPlayer());
                        }
                    } else {
                        action.getPlayer()
                                .sendMessage(
                                        StringUtils.translateAlternateColorCodes(
                                                "&cAn error occurred while getting name!"));
                    }
                });
    }

    @Override
    public void setUpMenu(
            Player player, ItemClickAction action, @Nullable Map<String, Object> info) {}
}
