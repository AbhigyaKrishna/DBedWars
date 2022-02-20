package org.zibble.dbedwars.action;

import org.zibble.dbedwars.action.translators.*;
import org.zibble.dbedwars.api.action.Action;
import org.zibble.dbedwars.api.action.ActionTranslationRegistry;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationRegistryImpl implements ActionTranslationRegistry {

    private final Map<Key<String>, ActionTranslator<?, ? extends Action>> registeredTranslators;

    public TranslationRegistryImpl() {
        this.registeredTranslators = new ConcurrentHashMap<>();
    }

    public void registerDefaults() {
        this.registerTranslation(new ActionBarActionTranslator());
        this.registerTranslation(new CommandActionTranslator());
        this.registerTranslation(new ConsoleCommandActionTranslator());
        this.registerTranslation(new FireworkActionTranslator());
        this.registerTranslation(new PotionActionTranslator());
        this.registerTranslation(new SendMessageActionTranslator());
        this.registerTranslation(new SoundActionTranslator());
        this.registerTranslation(new TeleportActionTranslator());
        this.registerTranslation(new TitleActionTranslator());
    }

    @Override
    public <T, K extends Action<T>> void registerTranslation(ActionTranslator<T, K> translator) {
        this.registeredTranslators.put(translator.getKey(), translator);
    }

    @Override
    public Collection<ActionTranslator<?, ? extends Action>> getRegisteredTranslations() {
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
    public ActionTranslator<?, ? extends Action> getTranslator(Key<String> key) {
        for (Map.Entry<Key<String>, ActionTranslator<?, ? extends Action>> entry : this.registeredTranslators.entrySet()) {
            if (entry.getKey().equals(key)) return entry.getValue();
        }
        return null;
    }

    @Override
    public ActionTranslator<?, ? extends Action> getTranslator(String key) {
        for (Map.Entry<Key<String>, ActionTranslator<?, ? extends Action>> entry : this.registeredTranslators.entrySet()) {
            if (entry.getKey().get().equalsIgnoreCase(key)) return entry.getValue();
        }
        return null;
    }
}
