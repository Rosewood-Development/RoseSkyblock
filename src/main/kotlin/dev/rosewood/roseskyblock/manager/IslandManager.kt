package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val _islandGroups = mutableMapOf<UUID, MutableMap<IslandWorldGroup, IslandGroup>>()
    val islandGroups: Map<UUID, Map<IslandWorldGroup, IslandGroup>>
        get() = this._islandGroups.toMap()

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

//    fun loadIslandData(owner: OfflinePlayer): CompletableFuture<Map<IslandWorldGroup, IslandGroup>> {
//
//    }
//
//    fun unloadIslandData(owner: OfflinePlayer) {
//
//    }

}
