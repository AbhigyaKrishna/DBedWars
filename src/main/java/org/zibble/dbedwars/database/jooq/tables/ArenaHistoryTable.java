package org.zibble.dbedwars.database.jooq.tables;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Multimap;
import org.jooq.Schema;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.jooq.DBedWarsSchema;
import org.zibble.dbedwars.database.jooq.binding.*;
import org.zibble.dbedwars.database.jooq.records.ArenaHistoryRecord;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ArenaHistoryTable extends TableImpl<ArenaHistoryRecord> {

    public static final ArenaHistoryTable ARENA_HISTORY = new ArenaHistoryTable();

    public final TableField<ArenaHistoryRecord, String> ID = createField(DSL.name("ID"), SQLDataType.VARCHAR(20).nullable(false), this);
    public final TableField<ArenaHistoryRecord, String> GAME_ID = createField(DSL.name("GAME_ID"), SQLDataType.VARCHAR(50).nullable(false), this);
    public final TableField<ArenaHistoryRecord, Multimap<Color, UUID>> TEAM = createField(DSL.name("TEAM"), SQLDataType.JSON.nullable(false), this, "", new TeamJsonConverter());
    public final TableField<ArenaHistoryRecord, Color> WINNER = createField(DSL.name("WINNER_TEAM"), SQLDataType.TINYINT.nullable(false), this, "", new ColorBinding());
    public final TableField<ArenaHistoryRecord, Duration> RUNTIME = createField(DSL.name("RUNTIME"), SQLDataType.INTERVAL.nullable(false), this, "", new DurationConverter());
    public final TableField<ArenaHistoryRecord, Instant> TIMESTAMP = createField(DSL.name("TIMESTAMP"), SQLDataType.INSTANT.nullable(false).defaultValue(Instant.now()), this);
    public final TableField<ArenaHistoryRecord, Map<UUID, Map<XMaterial, Integer>>> ITEM_PICKUP = createField(DSL.name("ITEM_PICKUP"), SQLDataType.JSON.nullable(false), this, "", new ItemPickupConverter());
    public final TableField<ArenaHistoryRecord, Map<UUID, ArenaHistory.DeathData>> DEATHS = createField(DSL.name("DEATHS"), SQLDataType.JSON.nullable(false), this, "", new DeathConverter());
    public final TableField<ArenaHistoryRecord, Multimap<UUID, Color>> BEDS_BROKEN = createField(DSL.name("BEDS_BROKEN"), SQLDataType.JSON.nullable(false), this, "", new BedBrokenConverter());

    public ArenaHistoryTable() {
        super(DSL.name("dbedwars_arena_history"));
    }

    @Override
    public Schema getSchema() {
        return DBedWarsSchema.SCHEMA;
    }

}
