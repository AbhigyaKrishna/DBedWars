package org.zibble.dbedwars.api.menu;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.exceptions.DuplicateActionDefinition;

import java.util.Optional;

public interface ActionRegistry {

    void register(@NotNull MenuActions menuActions) throws DuplicateActionDefinition;

    void register(@NotNull MenuActions... menuActions) throws DuplicateActionDefinition;

    void unregister(@NotNull String tag);

    void unregister(@NotNull MenuActions actions);

    boolean isRegistered(@NotNull String tag);

    boolean isRegistered(@NotNull MenuActions actions);

    Optional<MenuActions> getOptionalMenu(@NotNull String tag);

}
