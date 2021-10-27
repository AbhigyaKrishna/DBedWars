package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Countdown implements CommandArgument {

  private final DBedwars plugin;

  private Map<String, me.abhigya.dbedwars.api.game.spawner.Spawner> spawners = new HashMap<>();

  public Countdown(DBedwars plugin) {
    this.plugin = plugin;
  }

  @Override
  public String getName() {
    return "countdown";
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) return false;

    Player player = (Player) sender;

    if (args.length < 1) return false;

    return true;
  }

  @Override
  public List<String> tab(
      CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}
