package org.zibble.dbedwars.database.jooq;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

import java.util.Arrays;
import java.util.List;

public class DBedWarsSchema extends SchemaImpl {

    public static DBedWarsSchema SCHEMA = new DBedWarsSchema("DBedWars");

    private DBedWarsSchema(String name) {
        super(name, null);
    }

    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.INSTANCE;
    }

    @Override
    public List<Table<?>> getTables() {
        return Arrays.asList(PlayerStatTable.PLAYER_STAT, ArenaHistoryTable.ARENA_HISTORY, QuickBuyTable.QUICK_BUY);
    }

}
