package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.PotionAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import org.bukkit.entity.LivingEntity;

public class PotionActionTranslator implements ActionTranslator<LivingEntity> {

    @Override
    public Action<LivingEntity> serialize(String untranslated) {
        return new PotionAction(PotionEffectAT.valueOf(untranslated));
    }

    @Override
    public String deserialize(Action<LivingEntity> action) {
        return ((PotionAction) action).getPotionEffectAT().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("POTION");
    }

}
