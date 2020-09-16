package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import java.sql.Connection

class _1_Create_Table_Island : DataMigration(1) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island (
                    id INTEGER PRIMARY KEY,
                    group_id INTEGER NOT NULL,
                    world VARCHAR(100) NOT NULL,
                    spawn_x DOUBLE NOT NULL,
                    spawn_y DOUBLE NOT NULL,
                    spawn_z DOUBLE NOT NULL,
                    spawn_pitch FLOAT NOT NULL,
                    spawn_yaw FLOAT NOT NULL,
                    UNIQUE (group_id, world)
                )""".trimIndent()
            )
        }
    }

}
