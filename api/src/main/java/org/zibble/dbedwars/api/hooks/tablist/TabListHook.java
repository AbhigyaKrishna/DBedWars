package org.zibble.dbedwars.api.hooks.tablist;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.hooks.Hook;

import java.util.UUID;

public interface TabListHook extends Hook {

    void initTabForGame(@NotNull Arena arena);

    void updatePlayer(@NotNull UUID uuid, boolean isDead);

    void resetTab(@NotNull Arena arena);

    default void updateTeamElimination(@NotNull Team team){
        team.getPlayers().iterator().forEachRemaining(arenaPlayer -> {
            updatePlayer(arenaPlayer.getPlayer().getUniqueId(), true);
        });
    }


}
