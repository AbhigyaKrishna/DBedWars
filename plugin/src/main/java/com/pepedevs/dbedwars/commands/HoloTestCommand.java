package com.pepedevs.dbedwars.commands;

import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.task.HologramExpertRotateTask;
import com.pepedevs.dbedwars.task.HologramRotateTask;
import com.pepedevs.corelib.commands.CommandArgument;
import com.pepedevs.corelib.utils.hologram.Hologram;
import com.pepedevs.dbedwars.DBedwars;
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
        LinkedHashMap<LocationXYZYP, Integer> map = new LinkedHashMap<>();
        map.put(new LocationXYZYP(0, 0, 0, 90, 0), 10);
        System.out.println(map);
        HologramRotateTask task =
                new HologramExpertRotateTask(DBedwars.getInstance(), hologram3, map, (short) 50);
        task.start();
        return true;
    }

    @Override
    public List<String> tab(
            CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
