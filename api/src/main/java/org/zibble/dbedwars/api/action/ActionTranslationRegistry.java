package org.zibble.dbedwars.api.action;

import org.zibble.dbedwars.api.util.Key;

import java.util.Collection;

public interface ActionTranslationRegistry {

    <T, K extends Action<T>> void registerTranslation(ActionTranslator<T, K> action);

    Collection<ActionTranslator<?, ? extends Action>> getRegisteredTranslations();

    boolean isRegistered(Key<String> key);

    boolean isRegistered(String key);

    void unregisterTranslation(Key<String> key);

    void unregisterTranslation(String key);

    ActionTranslator<?, ? extends Action> getTranslator(Key<String> key);

    ActionTranslator<?, ? extends Action> getTranslator(String key);

}
