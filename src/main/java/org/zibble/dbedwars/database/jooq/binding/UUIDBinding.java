package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.zibble.dbedwars.utils.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

public class UUIDBinding extends BindingBase<UUID, UUID> {

    @Override
    public @NotNull Converter<UUID, UUID> converter() {
        return Converter.of(UUID.class, UUID.class, Function.identity(), Function.identity());
    }

    @Override
    public void set(SQLDialect dialect, PreparedStatement statement, int index, UUID value) throws SQLException {
        if (supportsUUID(dialect)) {
            statement.setObject(index, value);
        } else {
            statement.setBytes(index, DatabaseUtils.toByteArray(value));
        }
    }

    @Override
    public UUID get(SQLDialect dialect, ResultSet resultSet, int index) throws SQLException {
        if (supportsUUID(dialect)) {
            return resultSet.getObject(index, UUID.class);
        } else {
            return DatabaseUtils.fromByteArray(resultSet.getBytes(index));
        }
    }

    @Override
    public Field<?> inline(SQLDialect dialect, UUID value) {
        if (supportsUUID(dialect)) {
            return DSL.inline(value, SQLDataType.UUID);
        } else {
            return DSL.inline(DatabaseUtils.toByteArray(value), SQLDataType.BINARY(16));
        }
    }

    boolean supportsUUID(SQLDialect dialect) {
        return dialect == SQLDialect.POSTGRES || dialect == SQLDialect.HSQLDB;
    }

}
