package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.AbstractDataManager
import dev.rosewood.roseskyblock.island.Island
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.island.IslandMemberLevel
import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.sql.Statement
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.util.*

class DataManager(rosePlugin: RosePlugin) : AbstractDataManager(rosePlugin) {

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
                "INSERT INTO ${this.tablePrefix}island_member (group_id, player_uuid, member_level) VALUES (?, ?, ?)"
            connection.prepareStatement(insertMember).use {
                it.setInt(1, islandGroup.groupId)
                it.setString(2, ownerUniqueId.toString())
                it.setString(3, IslandMemberLevel.OWNER.name)
                it.executeUpdate()
            }
        }
        return islandGroup
    }

    fun saveNewIsland(islandGroup: IslandGroup, islandWorld: IslandWorld, spawnLocation: Location): Island {
        lateinit var island: Island
        this.databaseConnector.connect { connection ->
            val insertGroup =
                "INSERT INTO ${this.tablePrefix}island (group_id, world, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) VALUES (?, ?, ?, ?, ?, ?, ?)"
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

    fun getIslandGroup(player: OfflinePlayer, worldGroup: IslandWorldGroup): Optional<IslandGroup> {
        var islandGroup: Optional<IslandGroup> = Optional.empty()
        this.databaseConnector.connect { connection ->
            val selectGroup =
                "SELECT group_id, location_id FROM ${this.tablePrefix}island_group WHERE group_name = ? AND owner_uuid = ?"

            var groupId: Int = -1
            val islands = mutableListOf<Island>()
            val members = mutableMapOf<UUID, IslandMemberLevel>()

            connection.prepareStatement(selectGroup).use {
                it.setString(1, worldGroup.name)
                it.setString(2, player.uniqueId.toString())
                val result = it.executeQuery()
                if (result.next()) {
                    groupId = result.getInt("group_id")

                    islandGroup = Optional.of(
                        IslandGroup(
                            worldGroup,
                            groupId,
                            player.uniqueId,
                            result.getInt("location_id"),
                            islands,
                            members
                        )
                    )
                }
            }

            if (!islandGroup.isPresent)
                return@connect

            val selectIslands = "SELECT * FROM ${this.tablePrefix}island WHERE group_id = ?"
            connection.prepareStatement(selectIslands).use {
                it.setInt(1, groupId)
                val result = it.executeQuery()
                while (result.next()) {
                    val world = worldGroup.worlds.first { world -> world.worldName == result.getString("world") }
                    islands.add(
                        Island(
                            islandGroup.get(),
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

            val selectMembers = "SELECT * FROM ${this.tablePrefix}island_member WHERE group_id = ?"
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
