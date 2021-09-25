package me.abhigya.dbedwars.game.arena;

import me.Abhigya.core.events.CustomEvent;
import me.abhigya.dbedwars.api.events.game.PlayerBaseEnterEvent;
import me.abhigya.dbedwars.configuration.configurabletrap.ConfigurableTrap;
import me.abhigya.dbedwars.configuration.configurabletrap.TrapEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Trap {

    private String id;
    private ConfigurableTrap cfgTrap;

    private List<Consumer<CustomEvent>> actions;

    public Trap(String id) {
        this.id = id;
        this.actions = new ArrayList<>();
    }

    public Trap(ConfigurableTrap trap) {
        this(trap.getId());
        this.cfgTrap = trap;
    }

    public void trigger(CustomEvent event) {
        this.actions.forEach(c -> c.accept(event));
    }

    private void parseTrapAction() {
        TrapEnum.TriggerType trigger = cfgTrap.getTrigger();

        if (trigger == TrapEnum.TriggerType.ENEMY_BASE_ENTER_EVENT) {
            Consumer<CustomEvent> consumer = event -> {
                if (!(event instanceof PlayerBaseEnterEvent))
                    return;

                PlayerBaseEnterEvent e = (PlayerBaseEnterEvent) event;

            };
        }
    }

}
