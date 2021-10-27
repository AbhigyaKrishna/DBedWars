package me.abhigya.dbedwars.commands;

import me.Abhigya.core.commands.CommandArgument;
import me.Abhigya.core.util.hologram.Hologram;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.task.HologramRotateTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HoloTestCommand implements CommandArgument {
    @Override
    public String getName() {
        return "holotest";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean execute(
            CommandSender commandSender, Command command, String s, String[] strings) {
        /*Hologram hologram = DBedwars.getInstance().getHologramFactory().createHologram(UUID.randomUUID().toString(),((Player)commandSender).getLocation(),"BRUH");
        hologram.add(new ItemStack(Material.WOOD),false);
        hologram.spawn();
        new HologramRotateTask(DBedwars.getInstance(),hologram).startRotation(540,60,"reverse",false, 0.5F);
        Hologram hologram2 = DBedwars.getInstance().getHologramFactory().createHologram(UUID.randomUUID().toString(),((Player)commandSender).getLocation().clone().add(1,0,2),"BRUH");
        hologram2.add(new ItemStack(Material.WOOD),false);
        hologram2.spawn();
        new HologramRotateTask(DBedwars.getInstance(),hologram2).startRotation(540,60,"reverse",true, 0.5F);*/
        Hologram hologram3 =
                DBedwars.getInstance()
                        .getHologramFactory()
                        .createHologram(
                                UUID.randomUUID().toString(),
                                ((Player) commandSender).getLocation(),
                                "OK LOLL");
        hologram3.add(new ItemStack(Material.WOOD), false);
        hologram3.spawn();
        LinkedHashMap<Location, Integer> map = new LinkedHashMap<>();
        Location location =
                ((Player) commandSender)
                        .getLocation()
                        .clone()
                        .subtract(((Player) commandSender).getLocation());
        map.put(yawAdder(location).clone(), 10);
        map.put(yawAdder(location).clone(), 10);
        map.put(yawAdder(location).clone(), 10);
        map.put(yawAdder(location).clone(), 10);
        map.put(yawAdder(location).clone(), 10);
        map.put(yawAdder(location).clone(), 10);
        System.out.println(map);
        new HologramRotateTask(DBedwars.getInstance(), hologram3);
        return true;
    }

    private Location yawAdder(Location location) {
        location.setYaw(location.getYaw() + 90F);
        return location;
    }

    @Override
    public List<String> tab(
            CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
