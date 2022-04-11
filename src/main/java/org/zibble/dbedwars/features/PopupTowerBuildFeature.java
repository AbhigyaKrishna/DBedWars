package org.zibble.dbedwars.features;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.block.Chest;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.task.implementations.PopupTowerWorkload;
import xyz.xenondevs.particle.ParticleEffect;

public class PopupTowerBuildFeature extends org.zibble.dbedwars.api.feature.custom.PopupTowerBuildFeature {

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
                        ? SoundVP.of(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                        : cfgPopupTower.getSound().equals("")
                        ? SoundVP.of(XSound.ENTITY_CHICKEN_EGG, 0, 0)
                        : SoundVP.valueOf(cfgPopupTower.getSound())),
                ParticleEffectASC.of(ParticleEffect.CLOUD, 1, 0.2F),
                chest.getBlock(),
                placer.getTeam(),
                2));
        this.plugin.getThreadHandler().submitSync(() -> XBlock.setType(chest.getBlock(), XMaterial.AIR));
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}
