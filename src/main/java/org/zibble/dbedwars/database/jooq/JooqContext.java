package org.zibble.dbedwars.database.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.util.regex.Pattern;

public class JooqContext {

    static final Pattern MATCH_ALL_EXCEPT_INFORMATION_SCHEMA = Pattern.compile("^(?!INFORMATION_SCHEMA)(.*?)$");
    static final Pattern MATCH_ALL = Pattern.compile("^(.*?)$");
    static final String REPLACEMENT = "dbedwars_$0";

    private final SQLDialect dialect;

    public JooqContext(SQLDialect dialect) {
        this.dialect = dialect;
    }

    public DSLContext createContext(Connection connection) {
        return DSL.using(connection, dialect, new Settings()
                .withRenderSchema(false)
                .withRenderMapping(new RenderMapping()
                        .withSchemata(new MappedSchema()
                                .withInputExpression(MATCH_ALL_EXCEPT_INFORMATION_SCHEMA)
                                .withTables(new MappedTable()
                                        .withInputExpression(MATCH_ALL)
                                        .withOutput(REPLACEMENT)))));
    }

    public SQLDialect getDialect() {
        return dialect;
    }

}
