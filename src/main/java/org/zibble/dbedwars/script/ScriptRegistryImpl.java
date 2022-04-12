package org.zibble.dbedwars.script;

import org.zibble.dbedwars.api.script.ScriptTranslationRegistry;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.action.translators.*;
import org.zibble.dbedwars.script.condition.translator.PlayerConditionTranslator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScriptRegistryImpl implements ScriptTranslationRegistry {

    public static final Key TRAP = Key.of("trap");

    private final Map<Key, ActionRegistry> actionRegistry;
    private final Map<Key, ConditionRegistry> conditionRegistry;

    public ScriptRegistryImpl() {
        actionRegistry = new HashMap<>();
        conditionRegistry = new HashMap<>();
    }

    public void registerDefaults() {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.registerDefaults();
        this.addRegistry(GLOBAL, actionRegistry);

        ActionRegistry trapActionRegistry = new ActionRegistry();
        trapActionRegistry.registerDefaults();
        this.addRegistry(TRAP, trapActionRegistry);

        ConditionRegistry conditionRegistry = new ConditionRegistry();
        conditionRegistry.registerDefaults();
        this.addRegistry(GLOBAL, conditionRegistry);
    }

    @Override
    public ActionRegistry actionRegistry(Key key) {
        return this.actionRegistry.get(key);
    }

    public void addRegistry(Key key, ActionRegistry registry) {
        this.actionRegistry.put(key, registry);
    }

    @Override
    public ConditionRegistry conditionRegistry(Key key) {
        return this.conditionRegistry.get(key);
    }

    public void addRegistry(Key key, ConditionRegistry registry) {
        this.conditionRegistry.put(key, registry);
    }

    public static class ActionRegistry implements TranslationRegistry<ActionTranslator<?, ? extends Action>> {

        private final Map<Key, ActionTranslator<?, ? extends Action>> registeredTranslators;

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
        public boolean isRegistered(Key key) {
            for (Key stringKey : this.registeredTranslators.keySet()) {
                return stringKey.equals(key);
            }
            return false;
        }

        @Override
        public boolean isRegistered(String key) {
            for (Key stringKey : this.registeredTranslators.keySet()) {
                return stringKey.get().equals(key);
            }
            return false;
        }

        @Override
        public void unregisterTranslation(Key key) {
            this.registeredTranslators.remove(key);
        }

        @Override
        public void unregisterTranslation(String key) {
            this.registeredTranslators.remove(Key.of(key));
        }

        @Override
        public ActionTranslator<?, ? extends Action> getTranslator(Key key) {
            return this.registeredTranslators.get(key);
        }

        @Override
        public ActionTranslator<?, ? extends Action> getTranslator(String key) {
            return this.registeredTranslators.get(Key.of(key));
        }

    }

    public static class ConditionRegistry implements TranslationRegistry<ConditionTranslator<?, ? extends Condition>> {

        private final Map<Key, ConditionTranslator<?, ? extends Condition>> registeredConditions;

        public ConditionRegistry() {
            this.registeredConditions = Collections.synchronizedMap(new HashMap<>());
        }

        protected void registerDefaults() {
            this.registerTranslation(new PlayerConditionTranslator());
        }

        @Override
        public void registerTranslation(ConditionTranslator<?, ? extends Condition> translator) {
            this.registeredConditions.put(translator.getKey(), translator);
        }

        @Override
        public Collection<ConditionTranslator<?, ? extends Condition>> getRegisteredTranslations() {
            return Collections.unmodifiableCollection(this.registeredConditions.values());
        }

        @Override
        public boolean isRegistered(Key key) {
            for (Key stringKey : this.registeredConditions.keySet()) {
                return stringKey.equals(key);
            }
            return false;
        }

        @Override
        public boolean isRegistered(String key) {
            for (Key stringKey : this.registeredConditions.keySet()) {
                return stringKey.get().equals(key);
            }
            return false;
        }

        @Override
        public void unregisterTranslation(Key key) {
            this.registeredConditions.remove(key);
        }

        @Override
        public void unregisterTranslation(String key) {
            this.registeredConditions.remove(Key.of(key));
        }

        @Override
        public ConditionTranslator<?, ? extends Condition> getTranslator(Key key) {
            return this.registeredConditions.get(key);
        }

        @Override
        public ConditionTranslator<?, ? extends Condition> getTranslator(String key) {
            return this.registeredConditions.get(Key.of(key));
        }

    }

}
