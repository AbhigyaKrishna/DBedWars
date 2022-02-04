package com.pepedevs.dbedwars.api.action;

import com.pepedevs.dbedwars.api.util.Key;

import java.util.Collection;

public interface ActionTranslationRegistry {

    <T> void registerTranslation(ActionTranslator<T> action);

    Collection<ActionTranslator<?>> getRegisteredTranslations();

    boolean isRegistered(Key<String> key);

    boolean isRegistered(String key);

    void unregisterTranslation(Key<String> key);

    void unregisterTranslation(String key);

    ActionTranslator<?> getTranslator(Key<String> key);

    ActionTranslator<?> getTranslator(String key);

}
