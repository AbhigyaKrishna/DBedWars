package org.zibble.dbedwars.api.menu;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.exceptions.DuplicateActionDefinitionException;

import java.util.Optional;

public interface ActionRegistry {

    void register(@NotNull MenuActions menuActions) throws DuplicateActionDefinitionException;

    void register(@NotNull MenuActions... menuActions) throws DuplicateActionDefinitionException;

    void unregister(@NotNull String tag);

    void unregister(@NotNull MenuActions actions);

    boolean isRegistered(@NotNull String tag);

    boolean isRegistered(@NotNull MenuActions actions);

    Optional<MenuActions> getOptionalMenu(@NotNull String tag);

}
