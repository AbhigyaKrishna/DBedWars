package org.zibble.dbedwars.api.menu;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.exceptions.DuplicateActionDefinitionException;

import java.util.Optional;

public interface ActionRegistry {

    void register(@NotNull MenuAction menuActions) throws DuplicateActionDefinitionException;

    void register(@NotNull MenuAction... menuActions) throws DuplicateActionDefinitionException;

    void unregister(@NotNull String tag);

    void unregister(@NotNull MenuAction actions);

    boolean isRegistered(@NotNull String tag);

    boolean isRegistered(@NotNull MenuAction actions);

    Optional<MenuAction> getOptionalMenu(@NotNull String tag);

}
