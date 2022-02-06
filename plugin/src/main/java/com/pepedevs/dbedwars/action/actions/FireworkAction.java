package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.FireworkEffectAT;
import org.bukkit.Location;

public class FireworkAction implements Action<Location> {

    private final Location location;
    private final FireworkEffectAT fireworkEffectAT;

    public FireworkAction(FireworkEffectAT fireworkEffectAT, Location location) {
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

    public FireworkEffectAT getFireworkEffectAT() {
        return this.fireworkEffectAT;
    }

}
