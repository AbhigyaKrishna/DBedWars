package com.pepedevs.dbedwars.hooks.vault;

import com.pepedevs.dbedwars.api.hooks.economy.EconomyHook;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook extends PluginDependence implements EconomyHook {

    private Economy economy;

    public VaultHook() {
        super("Vault");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                this.enabled = false;
                return true;
            }
            this.economy = rsp.getProvider();
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into VaultAPI"));
        }
        return true;
    }


    @Override
    public double getBalance(@NotNull Player player) {
        return economy.getBalance(player);
    }

    @Override
    public void addBalance(@NotNull Player player, double balanceToAdd) {
        this.economy.depositPlayer(player, balanceToAdd);
    }

    @Override
    public void removeBalance(@NotNull Player player, double balanceToRemove) {
        this.economy.withdrawPlayer(player, balanceToRemove);
    }

    @Override
    public String getBalanceFormatted(@NotNull Player player) {
        return this.economy.format(this.getBalance(player));
    }

    @Override
    public void disable() {

    }

}
