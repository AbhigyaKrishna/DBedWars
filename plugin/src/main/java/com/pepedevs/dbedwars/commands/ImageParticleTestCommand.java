package com.pepedevs.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.task.ParticleImageDisplayTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
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
    public boolean execute(
            CommandSender commandSender, Command command, String s, String[] strings) {
        DBedwars.getInstance()
                .getImageHandler()
                .getLoadedImages()
                .forEach(
                        (key, colors) -> {
                            DBedwars.getInstance()
                                    .getThreadHandler()
                                    .addAsyncWork(
                                            new ParticleImageDisplayTask(
                                                    100,
                                                    new Dimension(2, 2),
                                                    ((Player) commandSender).getLocation(),
                                                    "x",
                                                    colors,
                                                    new ParticleBuilder(ParticleEffect.REDSTONE)
                                                            .setAmount(1)
                                                            .setSpeed(0)));
                        });
        return true;
    }

    @Override
    public List<String> tab(
            CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
