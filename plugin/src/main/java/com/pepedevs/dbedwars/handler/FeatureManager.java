package com.pepedevs.dbedwars.handler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.features.*;
import com.pepedevs.dbedwars.utils.Debugger;
import com.pepedevs.radium.utils.Acceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

public class FeatureManager {

    private final Multimap<Key<String>, BedWarsFeature> features = ArrayListMultimap.create();

    private final DBedwars plugin;

    public FeatureManager(DBedwars plugin) {
        this.plugin = plugin;
    }

    public void registerDefaults() {
        this.registerFeature(new ArenaEndFireworkFeature(this.plugin));
        this.registerFeature(new DeathAnimationFeature(this.plugin));
        this.registerFeature(new BedBugChaseFeature(this.plugin));
        this.registerFeature(new BedBugDisplayNameUpdateFeature(this.plugin));
        this.registerFeature(new DreamDefenderChaseFeature(this.plugin));
        this.registerFeature(new DreamDefenderDisplayNameUpdateFeature(this.plugin));
        this.registerFeature(new FireballLaunchFeature(this.plugin));
        this.registerFeature(new SpongePlaceFeature(this.plugin));
        this.registerFeature(new TNTPlaceFeature(this.plugin));
    }

    public void registerFeature(BedWarsFeature feature) {
        this.features.put(feature.getKey(), feature);
    }

    public void unregisterFeature(BedWarsFeature feature) {
        this.features.remove(Key.of(feature.getKey()), feature);
    }

    public Collection<BedWarsFeature> unregisterAllFeature(Key<String> featureKey) {
        return this.features.removeAll(featureKey);
    }

    public Collection<BedWarsFeature> getFeature(Key<String> featureName) {
        return this.features.get(featureName);
    }

    public Collection<BedWarsFeature> getAllFeatures() {
        return this.features.values();
    }

    public boolean hasFeature(Key<String> featureName) {
        return this.features.containsKey(featureName);
    }

    public <T extends BedWarsFeature> void runFeature(Key<String> featureName, Class<T> type, Acceptor<T> trigger) {
        EnumMap<FeaturePriority, Collection<BedWarsFeature>> map = new EnumMap<>(FeaturePriority.class);
        for (BedWarsFeature feature : this.getFeature(featureName)) {
            if (!map.containsKey(feature.getPriority()) || map.get(feature.getPriority()) == null) {
                map.put(feature.getPriority(), new ArrayList<>());
            }
            map.get(feature.getPriority()).add(feature);
        }

        for (FeaturePriority priority : FeaturePriority.ORDERED) {
            if (map.containsKey(priority) && map.get(priority) != null) {
                for (BedWarsFeature feature : map.get(priority)) {
                    if (!feature.getClass().equals(type)) continue;
                    if (!feature.isEnabled()) continue;
                    Debugger.debug("Running feature " + feature.getKey() + " with priority " + priority.name());
                    trigger.accept((T) feature);
                }
            }
        }
    }
}
