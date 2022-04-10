package org.zibble.dbedwars.hooks.cmi;

import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.hooks.cmi.nickname.CMINick;
import org.zibble.dbedwars.hooks.cmi.vanish.CMIVanish;

public class CMIHook extends PluginDependence {

    private CMINick cmiNick;
    private CMIVanish cmiVanish;

    public CMIHook() {
        super("CMI");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            this.cmiNick = new CMINick();
            this.cmiVanish = new CMIVanish();
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into CMI!"));
        }
        return true;
    }

    public CMINick getCmiNick() {
        return cmiNick;
    }

    public CMIVanish getCmiVanish() {
        return cmiVanish;
    }

}
