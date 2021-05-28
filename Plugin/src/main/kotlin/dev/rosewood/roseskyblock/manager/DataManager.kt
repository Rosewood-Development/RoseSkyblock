package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.AbstractDataManager
import dev.rosewood.roseskyblock.island.Island
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.island.IslandMemberLevel
import dev.rosewood.roseskyblock.island.IslandSettings
import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.sql.Statement
import java.util.UUID

@Suppress("unused")
class DataManager(rosePlugin: RosePlugin) : AbstractDataManager(rosePlugin) {

    /**
     * Save a new island world group into SQL
     *
     * @param worldGroup The worldgroup
     * @param ownerUniqueId The island group's owner ID
     * @param locationId The IslandGroup location
     * @return The owner's new IslandGroup.
     */
    fun saveNewIslandGroup(worldGroup: IslandWorldGroup, ownerUniqueId: UUID, locationId: Int): IslandGroup {
        lateinit var islandGroup: IslandGroup

        this.databaseConnector.connect { connection ->
            val insertGroup =
                "INSERT INTO ${this.tablePrefix}island_group (group_name, owner_uuid, location_id) VALUES (?, ?, ?)"
            connection.prepareStatement(insertGroup, Statement.RETURN_GENERATED_KEYS).use {
                it.setString(1, worldGroup.name)
                it.setString(2, ownerUniqueId.toString())
                it.setInt(3, locationId)
                it.executeUpdate()

                val keys = it.generatedKeys
                keys.next()

                val groupId = keys.getInt(1)
                islandGroup = IslandGroup(
                    worldGroup,
                    groupId,
                    ownerUniqueId,
                    locationId,
                    listOf(),
                    mapOf(Pair(ownerUniqueId, IslandMemberLevel.OWNER))
                )
            }

            val insertMember =
                "INSERT INTO ${this.tablePrefix}island_member (id, player_uuid, member_level) VALUES (?, ?, ?)"

            connection.prepareStatement(insertMember).use {
                it.setInt(1, islandGroup.groupId)
                it.setString(2, ownerUniqueId.toString())
                it.setString(3, IslandMemberLevel.OWNER.name)
                it.executeUpdate()
            }
        }
        return islandGroup
    }

    /**
     * Save a new island into the DB
     *
     * @param islandGroup The island's group
     * @param islandWorld The island's world
     * @param spawnLocation The island's spawn location
     * @return The island
     */
    fun saveNewIsland(islandGroup: IslandGroup, islandWorld: IslandWorld, spawnLocation: Location): Island {
        lateinit var island: Island

        this.databaseConnector.connect { connection ->
            val insertGroup =
                "INSERT INTO ${this.tablePrefix}island (id, world, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) VALUES (?, ?, ?, ?, ?, ?, ?)"

            connection.prepareStatement(insertGroup, Statement.RETURN_GENERATED_KEYS).use {
                it.setInt(1, islandGroup.groupId)
                it.setString(2, islandWorld.worldName)
                it.setDouble(3, spawnLocation.x)
                it.setDouble(4, spawnLocation.y)
                it.setDouble(5, spawnLocation.z)
                it.setFloat(6, spawnLocation.pitch)
                it.setFloat(7, spawnLocation.yaw)
                it.executeUpdate()

                val keys = it.generatedKeys
                keys.next()

                val islandId = keys.getInt(1)
                island = Island(islandGroup, islandId, islandWorld, spawnLocation)
            }

        }

        return island
    }

    /**
     * Save a new set of island settings into the DB
     *
     * @param settings The island settings.
     */
    fun saveNewIslandSettings(settings: IslandSettings) {
        this.databaseConnector.connect { connection ->
            val insertSettings =
                "INSERT INTO ${this.tablePrefix}settings (border) VALUES (?)"

            connection.prepareStatement(insertSettings).use {
                it.setString(1, settings.border.name)
                it.executeUpdate()
            }

        }
    }

    /**
     * Get a player's current island group
     *
     * @param player The player.
     * @param worldGroup The island's world group.
     * @return player's IslandGroup or null.
     */
    fun getIslandGroup(player: OfflinePlayer, worldGroup: IslandWorldGroup): IslandGroup? {
        var islandGroup: IslandGroup? = null

        this.databaseConnector.connect { connection ->
            val selectGroup =
                "SELECT id, location_id FROM ${this.tablePrefix}island_group WHERE group_name = ? AND owner_uuid = ?"

            var groupId: Int = -1
            val islands = mutableListOf<Island>()
            val members = mutableMapOf<UUID, IslandMemberLevel>()

            connection.prepareStatement(selectGroup).use {
                it.setString(1, worldGroup.name)
                it.setString(2, player.uniqueId.toString())
                val result = it.executeQuery()
                if (result.next()) {
                    groupId = result.getInt("id")

                    islandGroup = IslandGroup(
                        worldGroup,
                        groupId,
                        player.uniqueId,
                        result.getInt("location_id"),
                        islands,
                        members
                    )
                }
            }

            if (islandGroup == null)
                return@connect

            val selectIslands = "SELECT * FROM ${this.tablePrefix}island WHERE id = ?"
            connection.prepareStatement(selectIslands).use {
                it.setInt(1, groupId)
                val result = it.executeQuery()
                while (result.next()) {
                    val world = worldGroup.worlds.first { world -> world.worldName == result.getString("world") }
                    islands.add(
                        Island(
                            islandGroup!!,
                            result.getInt("id"),
                            world,
                            Location(
                                world.world,
                                result.getDouble("spawn_x"),
                                result.getDouble("spawn_y"),
                                result.getDouble("spawn_z"),
                                result.getFloat("spawn_yaw"),
                                result.getFloat("spawn_pitch")
                            )
                        )
                    )
                }
            }

            val selectMembers = "SELECT * FROM ${this.tablePrefix}island_member WHERE id = ?"

            connection.prepareStatement(selectMembers).use {
                it.setInt(1, groupId)
                val result = it.executeQuery()
                while (result.next()) {
                    val uuid = UUID.fromString(result.getString("player_uuid"))
                    val memberLevel = IslandMemberLevel.valueOf(result.getString("member_level"))
                    members[uuid] = memberLevel
                }
            }

        }

        return islandGroup
    }

}
