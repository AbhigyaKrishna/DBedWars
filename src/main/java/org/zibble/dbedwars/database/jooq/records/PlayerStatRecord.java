package org.zibble.dbedwars.database.jooq.records;

import org.jooq.impl.UpdatableRecordImpl;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;

public class PlayerStatRecord extends UpdatableRecordImpl<PlayerStatRecord> {

    public PlayerStatRecord() {
        super(PlayerStatTable.PLAYER_STAT);
    }

}
