package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.FireworkEffectC;
import org.bukkit.Location;

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
