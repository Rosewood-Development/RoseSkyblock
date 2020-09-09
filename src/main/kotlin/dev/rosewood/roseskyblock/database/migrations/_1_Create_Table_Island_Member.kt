package dev.rosewood.roseskyblock.database.migrations

import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.database.DatabaseConnector
import java.sql.Connection

class _1_Create_Table_Island_Member : DataMigration(1) {

    override fun migrate(connector: DatabaseConnector, connection: Connection, tablePrefix: String) {
        connection.createStatement().use {
            // The ID is purposely not auto_increment, we are going to be incrementing
            // the IDs manually to fill in gaps from deleted islands
            it.execute("""CREATE TABLE ${tablePrefix}island_member (
                    id INTEGER PRIMARY KEY,
                    island_id INTEGER NOT NULL,
                    player_uuid VARCHAR(36) NOT NULL,
                    member_level VARCHAR(20) NOT NULL,
                    UNIQUE (island_id, player_uuid)
                )""".trimIndent())
        }
    }

}
