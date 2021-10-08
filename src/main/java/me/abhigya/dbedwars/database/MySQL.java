package me.abhigya.dbedwars.database;

import me.abhigya.dbedwars.DBedwars;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class MySQL extends DatabaseBridge{

    private final me.Abhigya.core.database.sql.mysql.MySQL mySQLDatabase;

    public MySQL(DBedwars plugin, String host, int port, String database, @NotNull String username, @NotNull String password, boolean reconnect, boolean ssl) {
        super(plugin);
        mySQLDatabase = new me.Abhigya.core.database.sql.mysql.MySQL(host,port,database,username,password,reconnect,ssl);
    }

    @Override
    public void init() {
        try {
            mySQLDatabase.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQLDatabase.queryAsync("CREATE TABLE IF NOT EXISTS `PLAYER_STATS` (UUID varchar(37) primary key , NAME varchar(17), LEVEL int, LEVEL_PROGRESS double, COINS double, WINSTREAK int, POINTS double);");
    }

    @Override
    public void disconnect() {
        try {
            mySQLDatabase.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
