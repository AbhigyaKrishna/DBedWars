package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.game.ArenaCategory;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArenaCategory;
import org.zibble.dbedwars.game.GameManagerImpl;

public class ArenaCategoryImpl implements ArenaCategory {

    private final String name;
    private Message displayName;
    private Message description;
    private BwItemStack icon;
    private ScoreboardData scoreboardData;

    public ArenaCategoryImpl(String name, Message displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public static ArenaCategoryImpl fromConfig(GameManagerImpl gameManager, ConfigurableArenaCategory config) {
        ArenaCategoryImpl category = new ArenaCategoryImpl(config.getName(), ConfigMessage.from(config.getCustomName()));
        category.setDescription(ConfigMessage.from(config.getDescription()));
        category.setIcon(BwItemStack.valueOf(config.getIcon()));
        category.setScoreboardData(gameManager.getScoreboardData().get(config.getScoreboard()));
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Message getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(Message displayName) {
        this.displayName = displayName;
    }

    @Override
    public Message getDescription() {
        return description;
    }

    @Override
    public void setDescription(Message description) {
        this.description = description;
    }

    @Override
    public BwItemStack getIcon() {
        return icon;
    }

    @Override
    public void setIcon(BwItemStack icon) {
        this.icon = icon;
    }

    @Override
    public ScoreboardData getScoreboardData() {
        return scoreboardData;
    }

    @Override
    public void setScoreboardData(ScoreboardData scoreboardData) {
        this.scoreboardData = scoreboardData;
    }

}
