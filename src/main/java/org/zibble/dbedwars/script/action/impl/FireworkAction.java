package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Location;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;

public class FireworkAction implements Action<Location> {

    private final Location location;
    private final FireworkEffectC fireworkEffectAT;

    public FireworkAction(FireworkEffectC fireworkEffectAT, Location location) {
        this.location = location;
        this.fireworkEffectAT = fireworkEffectAT;
    }

    @Override
    public void execute() {
        this.fireworkEffectAT.spawn(this.getHandle());
    }

    @Override
    public Location getHandle() {
        return this.location;
    }

    public FireworkEffectC getFireworkEffectAT() {
        return this.fireworkEffectAT;
    }

}
