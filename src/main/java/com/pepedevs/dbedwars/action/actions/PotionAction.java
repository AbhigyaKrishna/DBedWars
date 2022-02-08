package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import org.bukkit.entity.LivingEntity;

public class PotionAction implements Action<LivingEntity> {

    private final LivingEntity entity;
    private final PotionEffectAT potionEffectAT;

    public PotionAction(PotionEffectAT potionEffectAT, LivingEntity entity) {
        this.entity = entity;
        this.potionEffectAT = potionEffectAT;
    }

    @Override
    public void execute() {
        this.potionEffectAT.applyTo(this.getHandle());
    }

    @Override
    public LivingEntity getHandle() {
        return this.entity;
    }

    public PotionEffectAT getPotionEffectAT() {
        return this.potionEffectAT;
    }
}
