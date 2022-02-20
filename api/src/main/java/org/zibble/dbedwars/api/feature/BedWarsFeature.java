package org.zibble.dbedwars.api.feature;

import org.zibble.dbedwars.api.util.*;

public abstract class BedWarsFeature implements Initializable, Tickable, Validable, Keyed<String> {

    protected final Key<String> key;
    protected boolean enabled = true;

    public BedWarsFeature(String key) {
        this.key = Key.of(key);
    }

    @Override
    public Key<String> getKey() {
        return key;
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
