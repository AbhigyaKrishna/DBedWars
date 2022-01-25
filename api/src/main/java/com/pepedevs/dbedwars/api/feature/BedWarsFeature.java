package com.pepedevs.dbedwars.api.feature;

import com.pepedevs.radium.utils.Initializable;
import com.pepedevs.radium.utils.Tickable;
import com.pepedevs.radium.utils.Validable;

public abstract class BedWarsFeature implements Initializable, Tickable, Validable, Runnable {

    protected final String name;

    public BedWarsFeature(String name) {
        this.name = name;
    }

    public void load() {
        //empty by default
    }

    public void unload() {
        //empty by default
    }

    public void enable() {
        //empty by default
    }

    public void disable() {
        //empty by default
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void tick() {

    }

    public abstract boolean isTickable();

    public abstract FeaturePriority getPriority();

}
