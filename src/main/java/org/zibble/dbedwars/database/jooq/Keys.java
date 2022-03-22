package org.zibble.dbedwars.database.jooq;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;

public class Keys {

    public static final UniqueKey<PlayerStatRecord> KEY_PLAYER_STAT_PRIMARY = Internal.createUniqueKey(PlayerStatTable.PLAYER_STAT, DSL.name("KEY_PLAYER_STAT_PRIMARY"), new TableField[]{PlayerStatTable.PLAYER_STAT.UUID}, true);

}
