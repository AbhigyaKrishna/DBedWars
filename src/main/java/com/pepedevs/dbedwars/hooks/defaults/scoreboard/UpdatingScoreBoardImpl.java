package com.pepedevs.dbedwars.hooks.defaults.scoreboard;

import com.pepedevs.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.entity.Player;


public class UpdatingScoreBoardImpl extends ScoreboardImpl implements UpdatingScoreboard {

    public UpdatingScoreBoardImpl(Player player, Message title, String name) {
        super(player, title, name);
    }

    @Override
    public void startUpdate() {

    }

    @Override
    public void cancelUpdate() {

    }
}
