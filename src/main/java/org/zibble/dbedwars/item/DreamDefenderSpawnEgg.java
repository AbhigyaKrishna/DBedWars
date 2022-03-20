package org.zibble.dbedwars.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.DreamDefenderChaseFeature;
import org.zibble.dbedwars.api.feature.custom.DreamDefenderDisplayNameUpdateFeature;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.utils.Util;

import java.util.Optional;

public class DreamDefenderSpawnEgg extends BedWarsActionItem {

    public static final FixedMetadataValue DREAM_DEFENDER_SPAWN_EGG_META =
            new FixedMetadataValue(DBedwars.getInstance(), true);
    private final DBedwars plugin;
    private final ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem;

    public DreamDefenderSpawnEgg(DBedwars plugin) {
        super(ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getDreamDefender().getName()),
                plugin.getConfigHandler().getCustomItems().getDreamDefender().getLore() == null ? null
                        : ConfigMessage.from(plugin.getConfigHandler().getCustomItems().getDreamDefender().getLore()),
                XMaterial.WOLF_SPAWN_EGG);
        this.plugin = plugin;
        cfgGolem = plugin.getConfigHandler().getCustomItems().getDreamDefender();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Arena arena = this.plugin.getGameManager().getArena(player.getWorld().getName());
        if (arena == null) return;
        Optional<ArenaPlayer> optionalArenaPlayer = arena.getAsArenaPlayer(player);
        if (!optionalArenaPlayer.isPresent()) return;
        ArenaPlayer arenaPlayer = optionalArenaPlayer.get();
        //TODO USELESS SPECTATOR CHECK
        Location spawn = playerInteractEvent.getClickedBlock().getLocation().add(0, 2, 0);
        IronGolem ironGolem = spawn.getWorld().spawn(spawn, IronGolem.class);
        Util.useItem(player);
        ironGolem.setMetadata("isDBedwarsGolem", DREAM_DEFENDER_SPAWN_EGG_META);
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.DREAM_DEFENDER_CHASE_FEATURE, DreamDefenderChaseFeature.class, new Acceptor<DreamDefenderChaseFeature>() {
            @Override
            public boolean accept(DreamDefenderChaseFeature dreamDefenderChaseFeature) {
                dreamDefenderChaseFeature.startChasing(ironGolem, arenaPlayer);
                return true;
            }
        });

        for (String effect : cfgGolem.getPotionEffects()) {
            if (effect == null || effect.equals("")) continue;
            PotionEffectAT effectAT = PotionEffectAT.valueOf(effect);
            if (effectAT != null) effectAT.applyTo(ironGolem);
        }
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.DREAM_DEFENDER_DISPLAY_NAME_UPDATE_FEATURE, DreamDefenderDisplayNameUpdateFeature.class, new Acceptor<DreamDefenderDisplayNameUpdateFeature>() {
            @Override
            public boolean accept(DreamDefenderDisplayNameUpdateFeature feature) {
                feature.start(ironGolem, arenaPlayer);
                return true;
            }
        });
    }

    public void onDeath(EntityDeathEvent event) {
        event.getDrops().clear();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("DREAM_DEFENDER");
    }

}
