package org.zibble.dbedwars.database.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {

    void accept(T t) throws SQLException;

}
