package org.zibble.dbedwars.guis.sign;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import org.bukkit.entity.Player;

public class SignMenuListener extends PacketListenerAbstract {

    private final SignMenuHandler handler;

    public SignMenuListener(SignMenuHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
            WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event);
            Player player = (Player) event.getPlayer();
            if (event.getPlayer() != null && this.handler.hasOpenMenu(player.getUniqueId())) {
                SignMenu menu = this.handler.getOpenMenu(player.getUniqueId());
                if (menu.getOnUpdate() != null) {
                    menu.getOnUpdate().accept(packet.getTextLines());
                }
            }
        }
    }

}
