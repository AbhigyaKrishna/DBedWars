package org.zibble.dbedwars.database.jooq;

import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;

import java.util.Collections;
import java.util.List;

public class DefaultCatalog extends CatalogImpl {

    public static final DefaultCatalog INSTANCE = new DefaultCatalog();

    private DefaultCatalog() {
        super("");
    }

    @Override
    public List<Schema> getSchemas() {
        return Collections.singletonList(DBedWarsSchema.SCHEMA);
    }

}
