package org.zibble.dbedwars.action.translators;

import org.bukkit.entity.LivingEntity;
import org.zibble.dbedwars.action.actions.PotionAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.util.Key;

public class PotionActionTranslator implements ActionTranslator<LivingEntity, PotionAction> {

    @Override
    public PotionAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        LivingEntity entity = null;
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof LivingEntity) {
                entity = (LivingEntity) placeholder.getValue();
            }
            if (placeholder.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
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
