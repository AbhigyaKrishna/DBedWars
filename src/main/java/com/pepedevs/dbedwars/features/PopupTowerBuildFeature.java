package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import com.pepedevs.dbedwars.task.PopupTowerWorkload;
import com.pepedevs.radium.particles.ParticleBuilder;
import com.pepedevs.radium.particles.ParticleEffect;
import com.pepedevs.radium.utils.xseries.XMaterial;
import com.pepedevs.radium.utils.xseries.XSound;
import org.bukkit.block.Chest;

public class PopupTowerBuildFeature extends com.pepedevs.dbedwars.api.feature.custom.PopupTowerBuildFeature {

    private final DBedwars plugin;

    public PopupTowerBuildFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void build(Chest chest, ArenaPlayer placer) {
        ConfigurableCustomItems.ConfigurablePopupTower cfgPopupTower = this.plugin.getConfigHandler().getCustomItems().getPopupTower();
        this.plugin.getThreadHandler().submitAsync(new PopupTowerWorkload(
                XMaterial.matchXMaterial(cfgPopupTower.getMainBlock()).get(),
                (cfgPopupTower.getSound() == null
                        ? new SoundVP(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                        : cfgPopupTower.getSound().equals("")
                        ? new SoundVP(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                        : SoundVP.valueOf(cfgPopupTower.getSound())),
                new ParticleBuilder(ParticleEffect.CLOUD)
                        .setAmount(1)
                        .setSpeed(0.2F),
                chest.getBlock(),
                placer.getTeam(),
                2));
        this.plugin.getThreadHandler().submitSync(new Runnable() {
            @Override
            public void run() {
                chest.getBlock().setType(XMaterial.AIR.parseMaterial());
            }
        });
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
