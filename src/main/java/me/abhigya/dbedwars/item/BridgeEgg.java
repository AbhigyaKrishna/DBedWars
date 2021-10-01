package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.EventUtils;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.task.BridgeEggWorkloadTask;
import org.bukkit.DyeColor;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class BridgeEgg extends PluginActionItem {

    private final int keepAliveTimeOut;
    private final int minDistanceFromPlayer;
    private final int maxDistanceFromPlayer;
    private final int maxDownStack;
    private final boolean isFlipBridgeEnabled;
    private final DBedwars plugin;

    public BridgeEgg(DBedwars plugin) {
        super(plugin, StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getName()), StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore() == null ? new ArrayList<>() : plugin.getConfigHandler().getCustomItems().getBridgeEgg().getLore()), XMaterial.EGG.parseMaterial());
        this.plugin = plugin;
        ConfigurableCustomItems.ConfigurableBridgeEgg confEgg = plugin.getConfigHandler().getCustomItems().getBridgeEgg();
        this.keepAliveTimeOut = confEgg.getKeepAliveTimeOut();
        this.maxDistanceFromPlayer = confEgg.getMaxDistanceFromPlayer();
        this.minDistanceFromPlayer = confEgg.getMinDistanceFromPlayer();
        this.maxDownStack = confEgg.getMaxDownStack();
        this.isFlipBridgeEnabled  = confEgg.isFlipBridgeEnabled();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent event) {
        if (!EventUtils.isRightClick(event.getAction())){
            return;
        }
        event.setCancelled(true);
        if(player.getItemInHand().isSimilar(this.toItemStack())){
            BwItemStack.removeItem(player,this.toItemStack());
        }
        Projectile projectile = player.launchProjectile(Egg.class);
        plugin.getThreadHandler().addSyncWork(new BridgeEggWorkloadTask(plugin,player, DyeColor.RED,projectile,minDistanceFromPlayer,keepAliveTimeOut,maxDistanceFromPlayer,maxDownStack,isFlipBridgeEnabled));
    }



}
