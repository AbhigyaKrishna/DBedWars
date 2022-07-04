package org.zibble.dbedwars.hooks.citizens;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

import java.util.HashSet;
import java.util.Set;

public class CitizensHook extends PluginDependence implements NPCFactory {

    private Set<BedWarsNPCImpl> npcs;
    private NPCListener listener;

    public CitizensHook() {
        super("Citizens");
    }

    @Override
    public void init() {
        this.npcs = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this.listener = new NPCListener(this), DBedWarsAPI.getApi().getPlugin());
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into Citizens!"));
        }
        return true;
    }


    @Override
    public EntityNPC createEntityNPC(Location location, EntityType type) {
        EntityNPCImpl npc = new EntityNPCImpl(this, location, type);
        this.npcs.add(npc);
        return npc;
    }

    @Override
    public PlayerNPC createPlayerNPC(Location location) {
        PlayerNPCImpl npc = new PlayerNPCImpl(this, location);
        this.npcs.add(npc);
        return npc;
    }

    public Set<BedWarsNPCImpl> getNpcs() {
        return npcs;
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this.listener);
        for (BedWarsNPCImpl npc : this.npcs) {
            npc.destroy();
        }
        this.npcs.clear();
    }

}
