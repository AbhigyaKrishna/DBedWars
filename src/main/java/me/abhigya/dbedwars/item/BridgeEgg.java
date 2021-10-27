package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.EventUtils;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaStatus;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.task.BridgeEggWorkloadTask;
import org.bukkit.DyeColor;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class BridgeEgg extends PluginActionItem {

  private final int keepAliveTimeOut;
  private final int minDistanceFromPlayer;
  private final int maxDistanceFromPlayer;
  private final int maxDownStack;
  private final boolean isFlipBridgeEnabled;
  private final DBedwars plugin;
  public static final FixedMetadataValue bridgeEggMeta =
      new FixedMetadataValue(DBedwars.getInstance(), true);

  public BridgeEgg(DBedwars plugin) {
    super(
        plugin,
        StringUtils.translateAlternateColorCodes(
            plugin.getConfigHandler().getCustomItems().getBridgeEgg().getName()),
        StringUtils.translateAlternateColorCodes(
            plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore() == null
                ? new ArrayList<>()
                : plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore()),
        XMaterial.EGG.parseMaterial());
    this.plugin = plugin;
    ConfigurableCustomItems.ConfigurableBridgeEgg confEgg =
        plugin.getConfigHandler().getCustomItems().getBridgeEgg();
    this.keepAliveTimeOut = confEgg.getKeepAliveTimeOut();
    this.maxDistanceFromPlayer = confEgg.getMaxDistanceFromPlayer();
    this.minDistanceFromPlayer = confEgg.getMinDistanceFromPlayer();
    this.maxDownStack = confEgg.getMaxDownStack();
    this.isFlipBridgeEnabled = confEgg.isFlipBridgeEnabled();
  }

  @Override
  public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent event) {
    if (!EventUtils.isRightClick(event.getAction())) {
      return;
    }
    if (!plugin.getGameManager().containsArena(player.getWorld().getName())) return;
    Arena arena = plugin.getGameManager().getArena(player.getWorld().getName());
    if (arena.getStatus() != ArenaStatus.RUNNING
        || !arena.getAsArenaPlayer(player).isPresent()
        || arena.getAsArenaPlayer(player).get().isSpectator()) return;
    event.setCancelled(true);
    BwItemStack.removeItem(player, this.toItemStack());
    Projectile egg = player.launchProjectile(Egg.class);
    egg.setMetadata("isDBedwarsEgg", bridgeEggMeta);
    plugin
        .getThreadHandler()
        .addSyncWork(
            new BridgeEggWorkloadTask(
                plugin,
                arena,
                player,
                DyeColor.RED,
                egg,
                minDistanceFromPlayer,
                keepAliveTimeOut,
                maxDistanceFromPlayer,
                maxDownStack,
                isFlipBridgeEnabled));
  }

  // TODO IDK HOW TO BLOCK CHICKEN SPAWNS HERE (UPDATE MEIN KARNEGE)

}
