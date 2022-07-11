package org.zibble.dbedwars.guis.sign;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SignMenuHandler {

    private final Map<UUID, SignMenu> OPEN_MENUS = new ConcurrentHashMap<>();
    private final SignMenuListener listener;

    public SignMenuHandler() {
        PacketEvents.getAPI().getEventManager().registerListener(this.listener = new SignMenuListener(this));
    }

    public void openMenu(Player player, String... text) {
        SignMenu menu = new SignMenu(text);
        OPEN_MENUS.put(player.getUniqueId(), menu);

        WrapperPlayServerOpenSignEditor packet = new WrapperPlayServerOpenSignEditor(Vector3i.zero());
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);

        if (menu.getText().length != 0) {
            // TODO: 07-07-2022  send block entity data packet
        }
    }

    public void destroy() {
        PacketEvents.getAPI().getEventManager().unregisterListener(listener);
    }

    public SignMenu getOpenMenu(UUID player) {
        return OPEN_MENUS.get(player);
    }

    public boolean hasOpenMenu(UUID uuid) {
        return OPEN_MENUS.containsKey(uuid);
    }

}
