package org.zibble.dbedwars.hooks.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderRegistry;

public class PAPIPlaceholderBridge extends PlaceholderExpansion {

    private final PlaceholderRegistry registry;

    public PAPIPlaceholderBridge(PlaceholderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.registry.getId();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", DBedWarsAPI.getApi().getPlugin().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return DBedWarsAPI.getApi().getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return this.registry.resolve(player, params);
    }

}
