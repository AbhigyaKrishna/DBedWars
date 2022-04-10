package org.zibble.dbedwars.handler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.util.function.Acceptor;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.features.*;
import org.zibble.dbedwars.utils.Debugger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

public class FeatureManager implements org.zibble.dbedwars.api.feature.FeatureManager {

    private final Multimap<Key, BedWarsFeature> features = ArrayListMultimap.create();

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

    @Override
    public void registerFeature(BedWarsFeature feature) {
        this.features.put(feature.getKey(), feature);
    }

    @Override
    public void unregisterFeature(BedWarsFeature feature) {
        this.features.remove(feature.getKey(), feature);
    }

    @Override
    public Collection<BedWarsFeature> unregisterAllFeature(Key featureKey) {
        return this.features.removeAll(featureKey);
    }

    @Override
    public Collection<BedWarsFeature> getFeature(Key featureName) {
        return this.features.get(featureName);
    }

    @Override
    public Collection<BedWarsFeature> getAllFeatures() {
        return this.features.values();
    }

    @Override
    public boolean hasFeature(Key featureKey) {
        return this.features.containsKey(featureKey);
    }

    @Override
    public <T extends BedWarsFeature> void runFeature(Key featureKey, Class<T> type, Acceptor<T> trigger) {
        EnumMap<FeaturePriority, Collection<BedWarsFeature>> map = new EnumMap<>(FeaturePriority.class);
        for (BedWarsFeature feature : this.getFeature(featureKey)) {
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
