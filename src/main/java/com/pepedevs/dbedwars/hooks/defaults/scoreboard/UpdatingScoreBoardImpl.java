package com.pepedevs.dbedwars.hooks.defaults.scoreboard;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.task.CancellableWorkload;
import com.pepedevs.dbedwars.api.util.Duration;
import org.bukkit.entity.Player;


public class UpdatingScoreBoardImpl extends ScoreboardImpl implements UpdatingScoreboard {

    private final CancellableWorkload cancellableWorkload;
    private Duration delayDuration;

    public UpdatingScoreBoardImpl(Player player, Message title, Duration delayDuration) {
        super(player, title);
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
        DBedwars.getInstance().getThreadHandler().submitAsync(this.cancellableWorkload);
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
