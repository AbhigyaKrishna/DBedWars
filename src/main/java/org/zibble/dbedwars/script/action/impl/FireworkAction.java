package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Location;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.script.action.Action;

public class FireworkAction implements Action {

    private final Location location;
    private final FireworkEffectC fireworkEffectAT;

    public FireworkAction(FireworkEffectC fireworkEffectAT, Location location) {
        this.location = location;
        this.fireworkEffectAT = fireworkEffectAT;
    }

    @Override
    public void execute() {
        this.fireworkEffectAT.spawn(this.getLocation());
    }

    public Location getLocation() {
        return this.location;
    }

    public FireworkEffectC getFireworkEffectAT() {
        return this.fireworkEffectAT;
    }

}
