package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.island.IslandGroup
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.util.runAsync
import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.concurrent.CompletableFuture
import org.bukkit.OfflinePlayer
import java.util.*

class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val islandGroups = Collections.synchronizedMap(mutableMapOf<UUID, MutableMap<IslandWorldGroup, IslandGroup>>())

    override fun reload() {
        TODO("Make this thingy do thingys")
    }

    override fun disable() {
        TODO("Make this thingy do thingys")
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
        copy.forEach {
            if (it == current) current++
        }

//        var current = 1
//        for (i in copy) {
//            if (i == current) {
//                current++
//            } else break
//        }

        return current
    }

    /**
     * Attempts to load islands for a player from the database, checks the cache first
     *
     * @param owner The owner of the islands to load
     * @return the islands that were loaded or the islands from cache
     */
    @Suppress("UNCHECKED_CAST")
    fun tryLoadIslands(owner: OfflinePlayer): CompletableFuture<Map<IslandWorldGroup, IslandGroup>> {
        if (islandGroups.containsKey(owner.uniqueId)) return CompletableFuture.completedFuture(islandGroups[owner.uniqueId])

        return CompletableFuture.supplyAsync {
            val dataManager = rosePlugin.getManager<DataManager>()

            rosePlugin.getManager<WorldManager>().worldGroups.associateWith {
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
        islandGroups.remove(owner.uniqueId)
    }
}
