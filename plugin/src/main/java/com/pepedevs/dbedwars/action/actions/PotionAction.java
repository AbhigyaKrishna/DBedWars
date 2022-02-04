package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import org.bukkit.entity.LivingEntity;

public class PotionAction implements Action<LivingEntity> {

    private final PotionEffectAT potionEffectAT;

    public PotionAction(PotionEffectAT potionEffectAT) {
        this.potionEffectAT = potionEffectAT;
    }

    @Override
    public void execute(LivingEntity entity) {
        potionEffectAT.applyTo(entity);
    }

    public PotionEffectAT getPotionEffectAT() {
        return potionEffectAT;
    }
}
