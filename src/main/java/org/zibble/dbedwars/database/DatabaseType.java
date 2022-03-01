package org.zibble.dbedwars.database;

public enum DatabaseType {
    SQLite,
    H2,
    PostGreSQL,
    MYSQL,
    HikariCP,
    MongoDB,

    // Oh boy whoever made this support will get a raise
    EXCEL,
    XML,
    YAML,
    JSON,
    ;
}
