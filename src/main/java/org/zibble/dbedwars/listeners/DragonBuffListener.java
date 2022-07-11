package org.zibble.dbedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.zibble.dbedwars.api.events.PostGameEventChangeEvent;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.script.action.impl.upgrade.DragonBuffAction;

import java.util.Map;

public class DragonBuffListener implements Listener {

    private final DragonBuffAction action = DragonBuffAction.INSTANCE;

    @EventHandler
    public void handleGameEvent(PostGameEventChangeEvent event) {
        for (Map.Entry<Team, DragonBuffAction.Action> entry : action.getActions().entrySet()) {
            if (event.getArena().equals(entry.getValue().getArena()) && event.getEvent().equals(entry.getValue().getEvent())) {
                entry.getValue().spawnDragons();
                action.getActions().remove(entry.getKey());
            }
        }
    }

}
