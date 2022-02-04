package com.pepedevs.dbedwars.action;

import com.pepedevs.dbedwars.api.action.ActionTranslationRegistry;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationRegistryImpl implements ActionTranslationRegistry {

    private final Map<Key<String>, ActionTranslator<?>> registeredTranslators;

    public TranslationRegistryImpl() {
        this.registeredTranslators = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void registerTranslation(ActionTranslator<T> translator) {
        this.registeredTranslators.put(translator.getKey(), translator);
    }

    @Override
    public Collection<ActionTranslator<?>> getRegisteredTranslations() {
        return Collections.unmodifiableCollection(this.registeredTranslators.values());
    }

    @Override
    public boolean isRegistered(Key<String> key) {
        for (Key<String> stringKey : this.registeredTranslators.keySet()) {
            return stringKey.equals(key);
        }
        return false;
    }

    @Override
    public boolean isRegistered(String key) {
        for (Key<String> stringKey : this.registeredTranslators.keySet()) {
            return stringKey.get().equals(key);
        }
        return false;
    }

    @Override
    public void unregisterTranslation(Key<String> key) {
        this.registeredTranslators.remove(key);
    }

    @Override
    public void unregisterTranslation(String key) {
        this.registeredTranslators.remove(Key.of(key));
    }

    @Override
    public ActionTranslator<?> getTranslator(Key<String> key) {
        for (Map.Entry<Key<String>, ActionTranslator<?>> entry : this.registeredTranslators.entrySet()) {
            if (entry.getKey().equals(key)) return entry.getValue();
        }
        return null;
    }

    @Override
    public ActionTranslator<?> getTranslator(String key) {
        for (Map.Entry<Key<String>, ActionTranslator<?>> entry : this.registeredTranslators.entrySet()) {
            if (entry.getKey().get().equals(key)) return entry.getValue();
        }
        return null;
    }
}