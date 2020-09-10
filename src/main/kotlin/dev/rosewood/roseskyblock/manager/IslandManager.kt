package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.world.IslandWorld
import kotlin.math.floor
import kotlin.math.sqrt
import org.bukkit.Location

class IslandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    override fun reload() {

    }

    override fun disable() {

    }

    // https://stackoverflow.com/a/19287714
    fun getNextIslandLocation(islandWorld: IslandWorld): Location {
        val islandCount = 0 // TODO: Query the database to see how many islands there are
        val islandDistance = 30 // TODO: Probably make this 1500 or something along those lines

        if (islandCount == 0)
            return Location(islandWorld.world, 0.0, islandWorld.islandHeight.toDouble(), 0.0)

        val n = islandCount - 1
        val r = floor((sqrt(n + 1.0) - 1) / 2) + 1
        val p = (8 * r * (r - 1)) / 2
        val en = r * 2
        val a = (1 + n - p) % (r * 8)

        var x = 0.0
        var z = 0.0

        when (floor(a / (r * 2)).toInt()) {
            0 -> {
                x = a - r
                z = -r
            }
            1 -> {
                x = r
                z = (a % en) - r
            }
            2 -> {
                x = r - (a % en)
                z = r
            }
            3 -> {
                x = -r
                z = r - (a % en)
            }
        }

        return Location(islandWorld.world, x * islandDistance, islandWorld.islandHeight.toDouble(), z * islandDistance)
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

}
