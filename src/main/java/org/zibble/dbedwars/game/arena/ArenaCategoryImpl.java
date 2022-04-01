package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.BwItemStack;

public class ArenaCategoryImpl {

    private final String name;
    private Message displayName;
    private String description;
    private BwItemStack icon;
    private ScoreboardData scoreboardData;

    public ArenaCategoryImpl(String name, Message displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public Message getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Message displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BwItemStack getIcon() {
        return icon;
    }

    public void setIcon(BwItemStack icon) {
        this.icon = icon;
    }

    public ScoreboardData getScoreboardData() {
        return scoreboardData;
    }

    public void setScoreboardData(ScoreboardData scoreboardData) {
        this.scoreboardData = scoreboardData;
    }

    public Arena[] getArenas() {
        // TODO: Implement
        return new Arena[0];
    }

}
