package org.zibble.dbedwars.script.action.impl;

import org.bukkit.entity.LivingEntity;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.script.action.Action;

public class PotionAction implements Action {

    private final LivingEntity entity;
    private final PotionEffectAT potionEffectAT;

    public PotionAction(PotionEffectAT potionEffectAT, LivingEntity entity) {
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
