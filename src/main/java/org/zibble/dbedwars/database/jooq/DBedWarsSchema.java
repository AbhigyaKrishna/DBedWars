package org.zibble.dbedwars.database.jooq;

import org.jooq.Catalog;
import org.jooq.impl.SchemaImpl;

public class DBedWarsSchema extends SchemaImpl {

    public static DBedWarsSchema SCHEMA;

    public static void init(String database) {
        SCHEMA = new DBedWarsSchema(database);
    }

    private DBedWarsSchema(String name) {
        super(name, null);
    }

    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.INSTANCE;
    }

}
