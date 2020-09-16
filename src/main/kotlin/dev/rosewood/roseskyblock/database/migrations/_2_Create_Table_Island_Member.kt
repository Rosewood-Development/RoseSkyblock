package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import java.sql.Connection

class _2_Create_Table_Island_Member : DataMigration(2) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        connection.createStatement().use {
            it.execute(
                """CREATE TABLE ${tablePrefix}island_member (
                    id INTEGER PRIMARY KEY,
                    group_id INTEGER NOT NULL,
                    player_uuid VARCHAR(36) NOT NULL,
                    member_level VARCHAR(20) NOT NULL,
                    UNIQUE (group_id, player_uuid)
                )""".trimIndent()
            )
        }
    }

}
