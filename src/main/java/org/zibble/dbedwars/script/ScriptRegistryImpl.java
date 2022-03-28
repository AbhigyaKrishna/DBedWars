package org.zibble.dbedwars.script;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.ScriptTranslationRegistry;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.action.translators.*;
import org.zibble.dbedwars.script.condition.translator.PlayerConditionTranslator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScriptRegistryImpl implements ScriptTranslationRegistry {

    private final Map<Key<String>, ActionRegistry> actionRegistry;
    private final Map<Key<String>, ConditionRegistry> conditionRegistry;

    public ScriptRegistryImpl() {
        actionRegistry = new HashMap<>();
        conditionRegistry = new HashMap<>();
    }

    public void registerDefaults() {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.registerDefaults();
        this.actionRegistry.put(Key.of("global"), actionRegistry);
        ConditionRegistry conditionRegistry = new ConditionRegistry();
        conditionRegistry.registerDefaults();
        this.conditionRegistry.put(Key.of("global"), conditionRegistry);
    }

    @Override
    public ActionRegistry actionRegistry(Key<String> key) {
        return this.actionRegistry.get(key);
    }

    @Override
    public ConditionRegistry conditionRegistry(Key<String> key) {
        return this.conditionRegistry.get(key);
    }

    public static class ActionRegistry implements TranslationRegistry<ActionTranslator<?, ? extends Action>> {

        private final Map<Key<String>, ActionTranslator<?, ? extends Action>> registeredTranslators;

        public ActionRegistry() {
            this.registeredTranslators = Collections.synchronizedMap(new HashMap<>());
        }

        protected void registerDefaults() {
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
        public void registerTranslation(ActionTranslator<?, ? extends Action> translator) {
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

    public static class ConditionRegistry implements TranslationRegistry<ConditionTranslator<?, ? extends Condition<?>>> {

        private final Map<Key<String>, ConditionTranslator<?, ? extends Condition>> registeredConditions;

        public ConditionRegistry() {
            this.registeredConditions = Collections.synchronizedMap(new HashMap<>());
        }

        protected void registerDefaults() {
            this.registerTranslation(new PlayerConditionTranslator());
        }

        @Override
        public void registerTranslation(ConditionTranslator<?, ? extends Condition<?>> translator) {
            this.registeredConditions.put(translator.getKey(), translator);
        }

        @Override
        public Collection<ConditionTranslator<?, ? extends Condition<?>>> getRegisteredTranslations() {
            return Collections.unmodifiableCollection(this.registeredConditions.values());
        }

        @Override
        public boolean isRegistered(Key<String> key) {
            for (Key<String> stringKey : this.registeredConditions.keySet()) {
                return stringKey.equals(key);
            }
            return false;
        }

        @Override
        public boolean isRegistered(String key) {
            for (Key<String> stringKey : this.registeredConditions.keySet()) {
                return stringKey.get().equals(key);
            }
            return false;
        }

        @Override
        public void unregisterTranslation(Key<String> key) {
            this.registeredConditions.remove(key);
        }

        @Override
        public void unregisterTranslation(String key) {
            this.registeredConditions.remove(Key.of(key));
        }

        @Override
        public ConditionTranslator<?, ? extends Condition> getTranslator(Key<String> key) {
            for (Map.Entry<Key<String>, ConditionTranslator<?, ? extends Condition>> entry : this.registeredConditions.entrySet()) {
                if (entry.getKey().equals(key)) return entry.getValue();
            }
            return null;
        }

        @Override
        public ConditionTranslator<?, ? extends Condition> getTranslator(String key) {
            for (Map.Entry<Key<String>, ConditionTranslator<?, ? extends Condition>> entry : this.registeredConditions.entrySet()) {
                if (entry.getKey().get().equalsIgnoreCase(key)) return entry.getValue();
            }
            return null;
        }

    }

}
