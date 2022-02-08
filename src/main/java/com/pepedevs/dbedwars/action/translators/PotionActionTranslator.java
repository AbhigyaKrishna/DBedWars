package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.PotionAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import org.bukkit.entity.LivingEntity;

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
