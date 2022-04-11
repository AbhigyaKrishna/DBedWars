package org.zibble.dbedwars.api.feature;

import org.zibble.dbedwars.api.util.mixin.Initializable;
import org.zibble.dbedwars.api.util.mixin.Tickable;
import org.zibble.dbedwars.api.util.mixin.Validable;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;

public abstract class BedWarsFeature implements Initializable, Tickable, Validable, Keyed {

    protected final Key key;
    protected boolean enabled = true;

    public BedWarsFeature(String key) {
        this.key = Key.of(key);
    }

    public BedWarsFeature(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
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

    public boolean isTickable() {
        return false;
    }

    public abstract FeaturePriority getPriority();

}
