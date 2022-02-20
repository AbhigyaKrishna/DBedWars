package org.zibble.dbedwars.hooks.tab;

import me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Duration;

import java.util.UUID;

public class UpdatingTabScoreboard extends TabScoreboard implements UpdatingScoreboard {

    private final CancellableWorkload cancellableWorkload;
    private Duration delayDuration;

    public UpdatingTabScoreboard(UUID playerID, TabHook tabHook, TabScoreboardHook scoreboardHook, ScoreboardImpl scoreboard, Duration delayDuration) {
        super(playerID, tabHook, scoreboardHook, scoreboard);
        this.delayDuration = delayDuration;
        this.cancellableWorkload = new CancellableWorkload() {
            long updatedLastAt = System.currentTimeMillis();
            @Override
            public void compute() {
                updatedLastAt = System.currentTimeMillis();
                setTitle(nextTitle());
                for(int i = 0; i < getLines().size(); i++){
                    setLine(i,nextLine(i));
                }
                update();
            }

            @Override
            public boolean shouldExecute(){
                if(isCancelled())
                    return false;

                return System.currentTimeMillis() - updatedLastAt >= delayDuration.toMillis();
            }
        };
    }

    @Override
    public void startUpdate() {
        DBedWarsAPI.getApi().getThreadHandler().submitAsync(this.cancellableWorkload);
    }

    @Override
    public void cancelUpdate() {
        this.cancellableWorkload.setCancelled(true);
    }

    @Override
    public Message nextLine(int index) {
        return null;
    }

    @Override
    public Message nextTitle() {
        return null;
    }

    @Override
    public Duration getDelay() {
        return this.delayDuration;
    }

    @Override
    public void setDelay(Duration delay) {
        this.delayDuration = delay;
    }
}