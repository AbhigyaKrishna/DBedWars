package org.zibble.dbedwars.hooks.tab.tablist;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TablistFormatManager;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.tablist.TabListHook;
import org.zibble.dbedwars.hooks.tab.TabHook;

public class TabTablistHook implements TabListHook {

    private final TabAPI api;
    private final TablistFormatManager tabManager;

    public TabTablistHook(TabHook hook) {
        this.api = hook.getApi();
        this.tabManager = this.api.getTablistFormatManager();
    }

    @Override
    public void setPrefix(Player player, String prefix) {
        this.tabManager.setPrefix(this.api.getPlayer(player.getUniqueId()), prefix);
    }

    @Override
    public void setName(Player player, String name) {
        this.tabManager.setName(this.api.getPlayer(player.getUniqueId()), name);
    }

    @Override
    public void setSuffix(Player player, String suffix) {
        this.tabManager.setSuffix(this.api.getPlayer(player.getUniqueId()), suffix);
    }

    @Override
    public void resetPrefix(Player player) {
        this.tabManager.resetPrefix(this.api.getPlayer(player.getUniqueId()));
    }

    @Override
    public void resetName(Player player) {
        this.tabManager.resetName(this.api.getPlayer(player.getUniqueId()));
    }

    @Override
    public void resetSuffix(Player player) {
        this.tabManager.resetSuffix(this.api.getPlayer(player.getUniqueId()));
    }

    @Override
    public String getPrefix(Player player) {
        return this.tabManager.getCustomPrefix(this.api.getPlayer(player.getUniqueId()));
    }

    @Override
    public String getName(Player player) {
        return this.tabManager.getCustomName(this.api.getPlayer(player.getUniqueId()));
    }

    @Override
    public String getSuffix(Player player) {
        return this.tabManager.getCustomSuffix(this.api.getPlayer(player.getUniqueId()));
    }

}
