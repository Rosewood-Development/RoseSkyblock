package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import java.sql.Connection

class CreateIslandGroupsTable : DataMigration(3) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island_group (
                    id INTEGER PRIMARY KEY,
                    group_name VARCHAR(100) NOT NULL,
                    owner_uuid VARCHAR(36) NOT NULL,
                    location_id INTEGER NOT NULL,
                    UNIQUE (group_name, owner_uuid),
                    UNIQUE (group_name, location_id)
                )""".trimIndent()
            )
        }
    }

}
