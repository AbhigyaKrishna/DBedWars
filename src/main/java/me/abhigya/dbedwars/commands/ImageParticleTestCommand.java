package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.utils.BetterImageDisplayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageParticleTestCommand implements CommandArgument {
    @Override
    public String getName() {
        return "particle";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings) {

        try {
            new BetterImageDisplayer(new File(DBedwars.getInstance().getDataFolder(),"test.png"),new Dimension(50,50)).start(DBedwars.getInstance(),0,1,((Player) commandSender).getLocation(),0.1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> tab(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }


}
