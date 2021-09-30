package me.abhigya.dbedwars.item;

import me.Abhigya.core.util.EventUtils;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.PotionEffectAT;
import me.abhigya.dbedwars.api.util.item.PluginActionItem;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.utils.FireBallUtil;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;


public class FireballItem extends PluginActionItem {

    private ConfigurableCustomItems.ConfigurableFireball configurableFireball;
    private ItemStack fireball;

    public FireballItem(DBedwars plugin) {
        super(plugin, StringUtils.translateAlternateColorCodes(plugin.getConfigHandler().getCustomItems().getFireball().getDisplayName()),
                (DBedwars.getInstance().getConfigHandler().getCustomItems().getFireball().getLore() == null ? new ArrayList<>() : DBedwars.getInstance().getConfigHandler().getCustomItems().getFireball().getLore()),
                XMaterial.FIRE_CHARGE.parseMaterial());
        this.configurableFireball = plugin.getConfigHandler().getCustomItems().getFireball();
        this.fireball = this.toItemStack();
    }

    @Override
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {
        if (EventUtils.isRightClick(playerInteractEvent.getAction()) || (configurableFireball.isLeftClickThrowEnabled() && EventUtils.isClickingBlock(playerInteractEvent.getAction()))) {
            playerInteractEvent.setCancelled(true);
            BwItemStack.removeItem(player, this.fireball);
            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setMetadata("plugin", new FixedMetadataValue(DBedwars.getInstance(), DBedwars.getInstance().getName()));
            fireball.setVelocity(fireball.getVelocity().multiply(configurableFireball.getSpeedMultiplier()));
            if (this.configurableFireball.isFixDirectionEnabled())
                FireBallUtil.setDirection(fireball, player.getEyeLocation().getDirection());
            fireball.setYield(configurableFireball.getExplosionYield());
            configurableFireball.getPotionEffects().forEach(s -> {
                PotionEffectAT effect = PotionEffectAT.valueOf(s);
                if (effect != null)
                    effect.applyTo(player);
            });
            fireball.setIsIncendiary(configurableFireball.isExplosionFireEnabled());
        }
    }
}
