package org.zibble.dbedwars.database.jooq.tables;

import org.jooq.Schema;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.database.jooq.records.ArenaHistoryRecord;
import org.zibble.dbedwars.database.jooq.DBedWarsSchema;
import org.zibble.dbedwars.database.jooq.binding.JsonConverter;

import java.sql.Time;
import java.sql.Timestamp;

public class ArenaTable extends TableImpl<ArenaHistoryRecord> {

    public final TableField<ArenaHistoryRecord, String> ID = createField(DSL.name("ID"), SQLDataType.VARCHAR(50), this);
    public final TableField<ArenaHistoryRecord, Json> TEAM = createField(DSL.name("TEAM"), SQLDataType.JSON, this, "", JsonConverter.CONVERTER);
    public final TableField<ArenaHistoryRecord, String> WINNER = createField(DSL.name("WINNER_TEAM"), SQLDataType.VARCHAR(50), this);
    public final TableField<ArenaHistoryRecord, Time> RUNTIME = createField(DSL.name("RUNTIME"), SQLDataType.TIME, this);
    public final TableField<ArenaHistoryRecord, Timestamp> TIMESTAMP = createField(DSL.name("TIMESTAMP"), SQLDataType.TIMESTAMP, this);
    public final TableField<ArenaHistoryRecord, Json> ITEM_PICKUP = createField(DSL.name("ITEM_PICKUP"), SQLDataType.JSON, this, "", JsonConverter.CONVERTER);
    public final TableField<ArenaHistoryRecord, Json> DEATHS = createField(DSL.name("DEATHS"), SQLDataType.JSON, this, "", JsonConverter.CONVERTER);
    public final TableField<ArenaHistoryRecord, Json> BEDS_BROKEN = createField(DSL.name("BEDS_BROKEN"), SQLDataType.JSON, this, "", JsonConverter.CONVERTER);

    public ArenaTable(String name) {
        super(DSL.name("arena_" + name));
    }

    @Override
    public Schema getSchema() {
        return DBedWarsSchema.SCHEMA;
    }

}
