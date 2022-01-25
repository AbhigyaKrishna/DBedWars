package com.pepedevs.dbedwars.api.feature;

import com.pepedevs.radium.utils.Initializable;
import com.pepedevs.radium.utils.Tickable;
import com.pepedevs.radium.utils.Validable;

public abstract class BedWarsFeature implements Initializable, Tickable, Validable {

    protected final String name;
    protected boolean enabled = true;

    public BedWarsFeature(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void load() {
        //empty by default
    }

    public void unload() {
        //empty by default
    }

    public void loadFromConfig() {
        //empty by default
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
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
