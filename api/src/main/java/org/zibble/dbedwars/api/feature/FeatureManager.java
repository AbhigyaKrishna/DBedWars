package org.zibble.dbedwars.api.feature;

import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.Key;

import java.util.Collection;

public interface FeatureManager {

    void registerFeature(BedWarsFeature feature);

    void unregisterFeature(BedWarsFeature feature);

    Collection<BedWarsFeature> unregisterAllFeature(Key<String> featureKey);

    Collection<BedWarsFeature> getFeature(Key<String> featureName);

    Collection<BedWarsFeature> getAllFeatures();

    boolean hasFeature(Key<String> featureKey);

    <T extends BedWarsFeature> void runFeature(Key<String> featureKey, Class<T> type, Acceptor<T> trigger);

}