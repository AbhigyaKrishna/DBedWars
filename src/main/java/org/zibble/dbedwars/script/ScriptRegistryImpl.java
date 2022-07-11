package org.zibble.dbedwars.script;

import org.zibble.dbedwars.api.script.ScriptTranslationRegistry;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionProvider;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.action.*;
import org.zibble.dbedwars.script.action.impl.*;
import org.zibble.dbedwars.script.action.impl.upgrade.DragonBuffAction;
import org.zibble.dbedwars.script.condition.ConditionPreProcessor;
import org.zibble.dbedwars.script.condition.translator.PlayerConditionTranslator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScriptRegistryImpl extends ScriptTranslationRegistry {

    public static final Key TRAP = Key.of("trap");
    public static final Key UPGRADE = Key.of("upgrade");
    public static final Key GAME_EVENT = Key.of("game_event");

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

        ActionRegistry upgradeActionRegistry = new ActionRegistry();
        upgradeActionRegistry.registerDefaults();
        upgradeActionRegistry.registerTranslation(DragonBuffAction.INSTANCE);
        this.addRegistry(UPGRADE, upgradeActionRegistry);

        ConditionRegistry conditionRegistry = new ConditionRegistry();
        conditionRegistry.registerDefaults();
        this.addRegistry(GAME_EVENT, conditionRegistry);
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

    @Override
    public ActionProvider actionProviderFromString(Key key, String action) {
        return key != null ? variables -> ActionPreProcessor.process(key, action, variables) : variables -> ActionPreProcessor.process(action, variables);
    }

    @Override
    public ConditionProvider conditionProviderFromString(Key key, String condition) {
        return key != null ? variables -> ConditionPreProcessor.process(key, condition, variables) : variables -> ConditionPreProcessor.process(condition, variables);
    }

    public void addRegistry(Key key, ConditionRegistry registry) {
        this.conditionRegistry.put(key, registry);
    }

    public static class ActionRegistry implements TranslationRegistry<ActionTranslator<? extends Action>> {

        private final Map<Key, ActionTranslator<? extends Action>> registeredTranslators;

        public ActionRegistry() {
            this.registeredTranslators = Collections.synchronizedMap(new HashMap<>());
        }

        protected void registerDefaults() {
            this.registerTranslation(new ActionBarAction());
            this.registerTranslation(new CommandAction());
            this.registerTranslation(new ConsoleCommandAction());
            this.registerTranslation(new FireworkAction());
            this.registerTranslation(new PotionAction());
            this.registerTranslation(new SendMessageAction());
            this.registerTranslation(new SoundAction());
            this.registerTranslation(new TeleportAction());
            this.registerTranslation(new TitleAction());
        }

        @Override
        public void registerTranslation(ActionTranslator<? extends Action> translator) {
            this.registeredTranslators.put(translator.getKey(), translator);
        }

        @Override
        public Collection<ActionTranslator<? extends Action>> getRegisteredTranslations() {
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
        public ActionTranslator<? extends Action> getTranslator(Key key) {
            return this.registeredTranslators.get(key);
        }

        @Override
        public ActionTranslator<? extends Action> getTranslator(String key) {
            return this.registeredTranslators.get(Key.of(key));
        }

    }

    public static class ConditionRegistry implements TranslationRegistry<ConditionTranslator<? extends Condition>> {

        private final Map<Key, ConditionTranslator<? extends Condition>> registeredConditions;

        public ConditionRegistry() {
            this.registeredConditions = Collections.synchronizedMap(new HashMap<>());
        }

        protected void registerDefaults() {
            this.registerTranslation(new PlayerConditionTranslator());
        }

        @Override
        public void registerTranslation(ConditionTranslator<? extends Condition> translator) {
            this.registeredConditions.put(translator.getKey(), translator);
        }

        @Override
        public Collection<ConditionTranslator<? extends Condition>> getRegisteredTranslations() {
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
        public ConditionTranslator<? extends Condition> getTranslator(Key key) {
            return this.registeredConditions.get(key);
        }

        @Override
        public ConditionTranslator<? extends Condition> getTranslator(String key) {
            return this.registeredConditions.get(Key.of(key));
        }

    }

}
