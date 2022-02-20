package org.zibble.dbedwars.api.game.view.attributes;

import org.bukkit.entity.Player;

import java.util.List;

public interface ClickCommand {

    List<String> getCommand();

    void runCommand(Player player);
}
