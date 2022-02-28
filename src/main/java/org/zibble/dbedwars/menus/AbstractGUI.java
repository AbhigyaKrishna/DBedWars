package org.zibble.dbedwars.menus;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.inventoryframework.menu.Menu;

public abstract class AbstractGUI {

    protected Component title;
    protected int rows;
    protected Menu menu;


    protected abstract void open(@NotNull final Player player);

}
