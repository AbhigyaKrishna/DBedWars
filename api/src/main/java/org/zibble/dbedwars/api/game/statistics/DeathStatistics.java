package org.zibble.dbedwars.api.game.statistics;

import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.DeathCause;

public class DeathStatistics extends Statistics<Arena, ArenaPlayer, DeathStatistics.DeathData> {

    public DeathStatistics(Arena tracker) {
        super(tracker);
    }

    public void put(ArenaPlayer player, ArenaPlayer killer, DeathCause cause) {
        this.put(player, DeathData.of(killer, cause));
    }

    public static class DeathData {

        private ArenaPlayer killer;
        private DeathCause cause;
        private DeathData(ArenaPlayer killer, DeathCause cause) {
            this.killer = killer;
            this.cause = cause;
        }

        public static DeathData of(ArenaPlayer killer, DeathCause cause) {
            return new DeathData(killer, cause);
        }

        public ArenaPlayer getKiller() {
            return killer;
        }

        public void setKiller(ArenaPlayer killer) {
            this.killer = killer;
        }

        public DeathCause getCause() {
            return cause;
        }

        public void setCause(DeathCause cause) {
            this.cause = cause;
        }

    }

}
