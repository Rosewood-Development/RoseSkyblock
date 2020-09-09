package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import java.sql.Connection

class _1_Create_Table_Island : DataMigration(1) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        connection.createStatement().use {
            // The ID is purposely not auto_increment, we are going to be incrementing
            // the IDs manually to fill in gaps from deleted islands
            it.execute(
                """CREATE TABLE ${tablePrefix}island (
                    id INTEGER PRIMARY KEY,
                    location_id INTEGER NOT NULL,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    spawn_x DOUBLE NOT NULL,
                    spawn_y DOUBLE NOT NULL,
                    spawn_z DOUBLE NOT NULL,
                    spawn_pitch FLOAT NOT NULL,
                    spawn_yaw FLOAT NOT NULL,
                    owner_uuid VARCHAR(36) NOT NULL,
                    schematic VARCHAR(200) NOT NULL,
                    UNIQUE (location_id, world)
                )""".trimIndent()
            )
        }
    }

}
