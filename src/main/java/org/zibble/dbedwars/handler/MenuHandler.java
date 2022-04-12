package org.zibble.dbedwars.handler;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.menus.actions.ActionRegistry;

public class MenuHandler implements org.zibble.dbedwars.api.handler.MenuHandler {


    private final DBedwars plugin;
    private final ActionRegistry actionRegistry;

    public MenuHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.actionRegistry = new ActionRegistry(this);
    }

    public DBedwars getPlugin() {
        return plugin;
    }

    @Override
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

}
