package com.pepedevs.dbedwars.api.hooks.scoreboard;

public interface UpdatingScoreboard extends Scoreboard{

    void startUpdate();

    void cancelUpdate();

}
