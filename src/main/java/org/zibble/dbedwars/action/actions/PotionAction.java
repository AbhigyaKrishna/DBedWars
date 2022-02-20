package org.zibble.dbedwars.action.actions;

import org.bukkit.entity.LivingEntity;
import org.zibble.dbedwars.api.action.Action;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;

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
