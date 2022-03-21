package org.zibble.dbedwars.script.action.translators;

import org.bukkit.entity.LivingEntity;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.action.impl.PotionAction;

public class PotionActionTranslator implements ActionTranslator<LivingEntity, PotionAction> {

    @Override
    public PotionAction serialize(String untranslated, ScriptVariable<?>... variables) {
        LivingEntity entity = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(LivingEntity.class)) {
                entity = (LivingEntity) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new PotionAction(PotionEffectAT.valueOf(untranslated), entity);
    }

    @Override
    public String deserialize(PotionAction action) {
        return action.getPotionEffectAT().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("POTION");
    }

}
