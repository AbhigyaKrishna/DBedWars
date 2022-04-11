package org.zibble.dbedwars.database.jooq.tables;

import org.jooq.Schema;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.zibble.dbedwars.database.data.QuickBuyData;
import org.zibble.dbedwars.database.jooq.DBedWarsSchema;
import org.zibble.dbedwars.database.jooq.Keys;
import org.zibble.dbedwars.database.jooq.binding.QuickBuyConverter;
import org.zibble.dbedwars.database.jooq.binding.UUIDBinding;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuickBuyTable extends TableImpl<QuickBuyRecord> {

    public static final QuickBuyTable QUICK_BUY = new QuickBuyTable();

    public final TableField<QuickBuyRecord, UUID> UUID = createField(DSL.name("UUID"), SQLDataType.UUID.notNull(), this, "", new UUIDBinding());
    public final TableField<QuickBuyRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(36).notNull(), this);
    public final TableField<QuickBuyRecord, QuickBuyData> QUICK_BUY_DATA = createField(DSL.name("QUICK_BUY_DATA"), SQLDataType.JSON.notNull(), this, "", new QuickBuyConverter());

    private QuickBuyTable() {
        super(DSL.name("dbedwars_quickbuy"));
    }

    @Override
    public Schema getSchema() {
        return DBedWarsSchema.SCHEMA;
    }

    @Override
    public UniqueKey<QuickBuyRecord> getPrimaryKey() {
        return Keys.KEY_QUICK_BUY_PRIMARY;
    }

    @Override
    public List<UniqueKey<QuickBuyRecord>> getKeys() {
        return Collections.singletonList(Keys.KEY_QUICK_BUY_PRIMARY);
    }

}