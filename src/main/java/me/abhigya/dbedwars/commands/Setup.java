package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Setup implements CommandArgument {

    private final DBedwars plugin;

    public Setup( DBedwars plugin ) {
        this.plugin = plugin;
    }

    @Override
    public String getName( ) {
        return "setup";
    }

    @Override
    public String getUsage( ) {
        return null;
    }

    @Override
    public boolean execute( CommandSender sender, Command command, String label, String[] args ) {

        if ( !( sender instanceof Player ) )
            return false;

        if ( args.length == 0 )
            return false;

        Player player = (Player) sender;

        if ( args[0].equalsIgnoreCase( "arena" ) ) {
            this.plugin.getGuiHandler( ).getGuis( ).get( "START_SETUP" ).open( null, null, player );
        }

        return true;
    }

    @Override
    public List< String > tab( CommandSender sender, Command command, String label, String[] args ) {
        return new ArrayList< String >( Collections.singleton( "arena" ) );
    }

}
