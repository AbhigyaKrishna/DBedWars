package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.Abhigya.core.util.StringUtils;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class End implements CommandArgument {

    private final DBedwars plugin;

    public End( DBedwars plugin ) {
        this.plugin = plugin;
    }

    @Override
    public String getName( ) {
        return "end";
    }

    @Override
    public String getUsage( ) {
        return null;
    }

    @Override
    public boolean execute( CommandSender sender, Command command, String s, String[] args ) {
        if ( !( sender instanceof Player ) )
            return false;

        Player player = (Player) sender;
        Arena arena = this.plugin.getGameManager( ).getArena( player.getWorld( ).getName( ) );
        if ( arena == null ) {
            player.sendMessage( StringUtils.translateAlternateColorCodes( "&cYou are not in a arena!" ) );
            return true;
        }

        arena.end( );
        return true;
    }

    @Override
    public List< String > tab( CommandSender commandSender, Command command, String s, String[] strings ) {
        return null;
    }

}
