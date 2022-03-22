package org.zibble.dbedwars.database.jooq.records;

import org.jooq.impl.TableRecordImpl;
import org.zibble.dbedwars.database.jooq.tables.ArenaTable;

public class ArenaHistoryRecord extends TableRecordImpl<ArenaHistoryRecord> {

    public ArenaHistoryRecord(ArenaTable table) {
        super(table);
    }

}
