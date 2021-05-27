package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import org.bukkit.OfflinePlayer
import java.util.Collections
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Suppress("unused")
class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val islandGroups = Collections.synchronizedMap(mutableMapOf<UUID, MutableMap<IslandWorldGroup, IslandGroup>>())

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
        val copy = existingIds.toMutableList()
        for (i in 0..copy.size) {
            while (copy[i] != i + 1) {
                if (copy[i] <= 0 || copy[i] > copy.size || copy[i] == copy[copy[i] - 1]) break
                val temp = copy[i]
                copy[i] = copy[temp - 1]
                copy[temp - 1] = temp
            }
        }
        for (i in 0..copy.size) if (copy[i] != i + 1) return i + 1
        return copy.size + 1
    }

    /**
     * Attempts to load islands for a player from the database, checks the cache first
     *
     * @param owner The owner of the islands to load
     * @return the islands that were loaded or the islands from cache
     */
    @Suppress("UNCHECKED_CAST")
    fun tryLoadIslands(owner: OfflinePlayer): CompletableFuture<Map<IslandWorldGroup, IslandGroup>> {
        if (this.islandGroups.containsKey(owner.uniqueId))
            return CompletableFuture.completedFuture(this.islandGroups[owner.uniqueId])

        return CompletableFuture.supplyAsync {
            val dataManager = this.rosePlugin.getManager<DataManager>()

            this.rosePlugin.getManager<WorldManager>().worldGroups.associateWith {
                dataManager.getIslandGroup(owner, it)
            }.filterValues { it != null } as Map<IslandWorldGroup, IslandGroup>
        }
    }

    /**
     * Attempts to unload islands for a player
     *
     * @param owner The player
     */
    fun tryUnloadIslands(owner: OfflinePlayer) {
        // TODO: Check if island is still active, if so, don't unload it
        this.islandGroups.remove(owner.uniqueId)
    }

}
