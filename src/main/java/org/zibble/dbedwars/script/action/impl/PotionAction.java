package org.zibble.dbedwars.script.action.impl;

import org.bukkit.entity.LivingEntity;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class PotionAction implements ActionTranslator<PotionAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        LivingEntity entity = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(LivingEntity.class)) {
                entity = (LivingEntity) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new Action(PotionEffectAT.valueOf(untranslated), entity);
    }

    @Override
    public String deserialize(Action action) {
        return action.getPotionEffectAT().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("POTION");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final LivingEntity entity;
        private final PotionEffectAT potionEffectAT;

        public Action(PotionEffectAT potionEffectAT, LivingEntity entity) {
            this.entity = entity;
            this.potionEffectAT = potionEffectAT;
        }

        @Override
        public void execute() {
            this.potionEffectAT.applyTo(this.getEntity());
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public PotionEffectAT getPotionEffectAT() {
            return this.potionEffectAT;
        }

    }

}
