package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.abhigya.dbedwars.item.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FireBallCommand implements CommandArgument {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length>0 && strings[0].equalsIgnoreCase("fireball")){
            if (commandSender instanceof Player){
                ((Player) commandSender).getInventory().addItem(CustomItems.FIREBALL.getItem().toItemStack());
            }
        }
        return true;
    }

    @Override
    public List<String> tab(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
