package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.PotionEffectAT;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.entity.Fireball;

public class FireballLaunchFeature extends com.pepedevs.dbedwars.api.feature.custom.FireballLaunchFeature {

    private final DBedwars plugin;

    public FireballLaunchFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void launch(Fireball fireball, ArenaPlayer launcher) {
        ConfigurableCustomItems.ConfigurableFireball cfgFireball = this.plugin.getConfigHandler().getCustomItems().getFireball();
        fireball.setVelocity(fireball.getVelocity().multiply(cfgFireball.getSpeedMultiplier()));
        if (cfgFireball.isFixDirectionEnabled()) Utils.setDirection(fireball, launcher.getPlayer().getEyeLocation().getDirection());
        fireball.setYield(cfgFireball.getExplosionYield());
        for (String s : cfgFireball.getPotionEffects()) {
            if (s == null || s.trim().equals("")) return;
            PotionEffectAT effect = PotionEffectAT.valueOf(s);
            if (effect != null) effect.applyTo(launcher.getPlayer());
        }
        fireball.setIsIncendiary(cfgFireball.isExplosionFireEnabled());
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

}
