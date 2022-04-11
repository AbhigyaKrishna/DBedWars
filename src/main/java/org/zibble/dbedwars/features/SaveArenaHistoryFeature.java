package org.zibble.dbedwars.features;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.utils.DatabaseUtil;

public class SaveArenaHistoryFeature extends org.zibble.dbedwars.api.feature.custom.SaveArenaHistoryFeature {

    private final DBedwars plugin;

    public SaveArenaHistoryFeature(DBedwars plugin) {
        this.plugin = plugin;
        this.enabled = this.plugin.getConfigHandler().getDatabase().getMatchHistory().isEnabled();
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public boolean save(Arena arena, Color winner) {
        ArenaHistory history = DatabaseUtil.createHistory(arena, winner);
        this.plugin.getDatabaseBridge().insertArenaHistory(history);
        return true;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}
