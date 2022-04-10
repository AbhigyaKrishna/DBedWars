package org.zibble.dbedwars.hooks.citizens;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.util.ClickType;

public class NPCListener implements Listener {

    private final CitizensHook hook;

    protected NPCListener(CitizensHook hook) {
        this.hook = hook;
    }

    @EventHandler(ignoreCancelled = true)
    public void handleNpcClick(NPCLeftClickEvent event) {
        for (BedWarsNPCImpl npc : hook.getNpcs()) {
            if (event.getNPC().equals(npc.getCitizensNPC())) {
                ClickType type = event.getClicker().isSneaking() ? ClickType.SHIFT_LEFT_CLICK : ClickType.LEFT_CLICK;
                for (ClickAction clickAction : npc.getClickActions()) {
                    clickAction.onClick(event.getClicker(), type);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void handleNpcClick(NPCRightClickEvent event) {
        for (BedWarsNPCImpl npc : hook.getNpcs()) {
            if (event.getNPC().equals(npc.getCitizensNPC())) {
                ClickType type = event.getClicker().isSneaking() ? ClickType.SHIFT_RIGHT_CLICK : ClickType.RIGHT_CLICK;
                for (ClickAction clickAction : npc.getClickActions()) {
                    clickAction.onClick(event.getClicker(), type);
                }
            }
        }
    }

}
