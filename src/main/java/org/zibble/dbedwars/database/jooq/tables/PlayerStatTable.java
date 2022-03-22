package org.zibble.dbedwars.database.jooq.tables;

import com.google.gson.JsonElement;
import org.jooq.JSON;
import org.jooq.Schema;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.zibble.dbedwars.database.data.PersistentStat;
import org.zibble.dbedwars.database.jooq.DBedWarsSchema;
import org.zibble.dbedwars.database.jooq.Keys;
import org.zibble.dbedwars.database.jooq.binding.PersistentStatConverter;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.binding.UUIDBinding;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerStatTable extends TableImpl<PlayerStatRecord> {

    public static final String DEFAULT_JSON = "{\"total\": 0, \"monthly\": 0, \"weekly\": 0, \"daily\": 0}";

    public static final PlayerStatTable PLAYER_STAT = new PlayerStatTable();

    public final TableField<PlayerStatRecord, UUID> UUID = createField(DSL.name("UUID"), SQLDataType.UUID.nullable(false), this, "", new UUIDBinding());
    public final TableField<PlayerStatRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(16).nullable(false), this);
    public final TableField<PlayerStatRecord, Integer> LEVEL = createField(DSL.name("LEVEL"), SQLDataType.INTEGER.nullable(false).defaultValue(0), this);
    public final TableField<PlayerStatRecord, Double> LEVEL_PROGRESS = createField(DSL.name("LEVEL_PROGRESS"), SQLDataType.DOUBLE.nullable(false).defaultValue(0.0), this);
    public final TableField<PlayerStatRecord, Double> COINS = createField(DSL.name("COINS"), SQLDataType.DOUBLE.nullable(false).defaultValue(0.0), this);
    public final TableField<PlayerStatRecord, Short> WINSTREAK = createField(DSL.name("WINSTREAK"), SQLDataType.SMALLINT.nullable(false).defaultValue((short) 0), this);
    public final TableField<PlayerStatRecord, Double> POINTS = createField(DSL.name("KILLS"), SQLDataType.DOUBLE.nullable(false).defaultValue(0.0), this);
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> KILLS = createField(DSL.name("KILLS"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("kills", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> FINAL_KILLS = createField(DSL.name("FINAL_KILLS"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("final_kills", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> DEATHS = createField(DSL.name("DEATHS"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("deaths", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> BED_BROKEN = createField(DSL.name("BED_BROKEN"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("bed_broken", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> BED_LOST = createField(DSL.name("BED_LOST"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("bed_lost", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Integer>> WINS = createField(DSL.name("WINS"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("wins", JsonElement::getAsInt));
    public final TableField<PlayerStatRecord, PersistentStat<Long>> PLAYED = createField(DSL.name("PLAYED"), SQLDataType.JSON.nullable(false).defaultValue(JSON.valueOf(DEFAULT_JSON)), this, "", new PersistentStatConverter<>("played", JsonElement::getAsLong));

    public PlayerStatTable() {
        super(DSL.name("dbedwars_player_stats"));
    }

    @Override
    public Schema getSchema() {
        return DBedWarsSchema.SCHEMA;
    }

    @Override
    public UniqueKey<PlayerStatRecord> getPrimaryKey() {
        return Keys.KEY_PLAYER_STAT_PRIMARY;
    }

    @Override
    public List<UniqueKey<PlayerStatRecord>> getKeys() {
        return Collections.singletonList(Keys.KEY_PLAYER_STAT_PRIMARY);
    }

}
