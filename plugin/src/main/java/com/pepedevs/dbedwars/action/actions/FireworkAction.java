package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.FireworkEffectAT;
import org.bukkit.Location;

public class FireworkAction implements Action<Location> {

    private final FireworkEffectAT fireworkEffectAT;

    public FireworkAction(FireworkEffectAT fireworkEffectAT) {
        this.fireworkEffectAT = fireworkEffectAT;
    }

    @Override
    public void execute(Location location) {
        this.fireworkEffectAT.spawn(location);
    }

    public FireworkEffectAT getFireworkEffectAT() {
        return fireworkEffectAT;
    }

}
