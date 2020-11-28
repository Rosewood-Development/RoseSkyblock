package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import org.bukkit.entity.Player

class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    override fun reload() {

    }

    override fun disable() {

    }


    /**
     * Gets the smallest positive integer greater than 0 from a list
     *
     * @param existingIds The list containing non-available ids
     * @return The smallest positive integer not in the given list
     */
    fun getNextIslandId(existingIds: Collection<Int>): Int {
        val copy = existingIds.sorted().toMutableList()
        copy.removeIf { it <= 0 }

        var current = 1
        for (i in copy) {
            if (i == current) {
                current++
            } else break
        }

        return current
    }

    /**
     * Checks if the player already has an island.
     *
     * @param player The player being checked.
     * @return true if player has an Island.
     */
    fun hasIsland(player: Player): Boolean {
        var hasIsland = false
        val dataManager = this.rosePlugin.getManager(DataManager::class.java)

        dataManager.databaseConnector.connect { connection ->
            val checkIsland = "SELECT owner_uuid FROM ${dataManager.tablePrefix}island_group WHERE owner_uuid = ?"
            connection.prepareStatement(checkIsland).use {
                it.setString(1, player.uniqueId.toString())
                val result = it.executeQuery()
                if (result.next())
                    hasIsland = result.getBoolean(1)
            }
        }

        return hasIsland
    }
}
