package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import dev.rosewood.rosegarden.database.MySQLConnector
import java.sql.Connection


class _1_CreateInitialTables : DataMigration(1) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        val autoIncrement = if (connector is MySQLConnector) " AUTO_INCREMENT" else ""

        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island (
                    id INTEGER PRIMARY KEY$autoIncrement,
                    group_id INTEGER NOT NULL,
                    world VARCHAR(100) NOT NULL,
                    spawn_x DOUBLE NOT NULL,
                    spawn_y DOUBLE NOT NULL,
                    spawn_z DOUBLE NOT NULL,
                    spawn_pitch FLOAT NOT NULL,
                    spawn_yaw FLOAT NOT NULL,
                    UNIQUE (id, world),
                    FOREIGN KEY (group_id) REFERENCES island_group(id)
                )""".trimIndent()
            )
        }

        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island_member (
                    id INTEGER PRIMARY KEY$autoIncrement,
                    group_id VARCHAR(36) NOT NULL,
                    player_uuid VARCHAR(36) NOT NULL,
                    member_level VARCHAR(20) NOT NULL,
                    UNIQUE (id, player_uuid),
                    FOREIGN KEY (group_id) REFERENCES island_group(id)
                )""".trimIndent()
            )
        }

        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island_group (
                    id VARCHAR(36) PRIMARY KEY$autoIncrement,
                    group_name VARCHAR(100) NOT NULL,
                    owner_uuid VARCHAR(36) NOT NULL,
                    location_id INTEGER NOT NULL,
                    UNIQUE (group_name, owner_uuid),
                    UNIQUE (group_name, location_id)
                )""".trimIndent()
            )
        }

        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island_settings (
                    id VARCHAR(36) PRIMARY KEY$autoIncrement,
                    border VARCHAR(10) NOT NULL
                )""".trimIndent()
            )
        }
    }

}
