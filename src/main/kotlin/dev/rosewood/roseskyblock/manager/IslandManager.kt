package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.util.runAsync
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.concurrent.CompletableFuture
import org.bukkit.OfflinePlayer
import java.util.*

class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val _islandGroups = Collections.synchronizedMap(mutableMapOf<UUID, MutableMap<IslandWorldGroup, IslandGroup>>())
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

    /**
     * Attempts to load islands for a player from the database, checks the cache first
     *
     * @param owner The owner of the islands to load
     * @return the islands that were loaded or the islands from cache
     */
    fun tryLoadIslands(owner: OfflinePlayer): CompletableFuture<Map<IslandWorldGroup, IslandGroup>> {
        if (this.islandGroups.containsKey(owner.uniqueId))
            return CompletableFuture.completedFuture(this.islandGroups[owner.uniqueId])

        val future = CompletableFuture<Map<IslandWorldGroup, IslandGroup>>()
        this.rosePlugin.runAsync {
            val dataManager = this.rosePlugin.getManager(DataManager::class)
            val islands = mutableMapOf<IslandWorldGroup, IslandGroup>()
            this.rosePlugin.getManager(WorldManager::class).worldGroups.forEach { worldGroup ->
                dataManager.getIslandGroup(owner, worldGroup).ifPresent {
                    islands[worldGroup] = it
                }
            }
            future.complete(islands)
        }
        return future
    }

    /**
     * Attempts to unload islands for a player
     *
     * @param owner The player
     */
    fun tryUnloadIslands(owner: OfflinePlayer) {
        // TODO: Check if island is still active, if so, don't unload it
        this._islandGroups.remove(owner.uniqueId)
    }

}
