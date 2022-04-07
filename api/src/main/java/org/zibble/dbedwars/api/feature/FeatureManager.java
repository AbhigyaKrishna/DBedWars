package org.zibble.dbedwars.api.feature;

import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Collection;

public interface FeatureManager {

    void registerFeature(BedWarsFeature feature);

    void unregisterFeature(BedWarsFeature feature);

    Collection<BedWarsFeature> unregisterAllFeature(Key featureKey);

    Collection<BedWarsFeature> getFeature(Key featureName);

    Collection<BedWarsFeature> getAllFeatures();

    boolean hasFeature(Key featureKey);

    <T extends BedWarsFeature> void runFeature(Key featureKey, Class<T> type, Acceptor<T> trigger);

}
