package org.zibble.dbedwars.features;

import org.bukkit.entity.Fireball;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.utils.Utils;

public class FireballLaunchFeature extends org.zibble.dbedwars.api.feature.custom.FireballLaunchFeature {

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
