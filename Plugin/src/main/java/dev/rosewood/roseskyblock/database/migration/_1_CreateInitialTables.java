package dev.rosewood.roseskyblock.database.migration;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.MySQLConnector;

import java.sql.Connection;
import java.sql.SQLException;

public class _1_CreateInitialTables extends DataMigration {

    public _1_CreateInitialTables() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        String autoIncrement = connector instanceof MySQLConnector ? " AUTO_INCREMENT" : "";

        connection.createStatement().execute("CREATE TABLE " + tablePrefix + "island (" +
                "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                "group_id INTEGER NOT NULL, " +
                "world VARCHAR(100) NOT NULL, " +
                "spawn_x DOUBLE NOT NULL, " +
                "spawn_y DOUBLE NOT NULL, " +
                "spawn_z DOUBLE NOT NULL, " +
                "spawn_pitch DOUBLE NOT NULL, " +
                "spawn_yaw FLOAT NOT NULL, " +
                "UNIQUE (id, world), " +
                "FOREIGN KEY (group_id) REFERENCES island_group(id))");

        connection.createStatement().execute("CREATE TABLE " + tablePrefix + "island_member (" +
                "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                "group_id VARCHAR(36) NOT NULL, " +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "member_level VARCHAR(20) NOT NULL, " +
                "UNIQUE (ID, player_uuid), " +
                "FOREIGN KEY (group_id) REFERENCES island_group(id)");

        connection.createStatement().execute("CREATE TABLE " + tablePrefix + "island_group (" +
                "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                "group_name VARCHAR(100) NOT NULL, " +
                "owner_uuid VARCHAR(36) NOT NULL, " +
                "location_id INTEGER NOT NULL, " +
                "UNIQUE (group_name, owner_uuid), " +
                "UNIQUE (group_name, location_id))");

        connection.createStatement().execute("CREATE TABLE " + tablePrefix + "island_settings (" +
                "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                "border VARCHAR(10) NOT NULL)"
        );
    }

}
