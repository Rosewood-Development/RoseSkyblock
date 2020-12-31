package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.util.getNextIslandLocation
import dev.rosewood.roseskyblock.world.IslandWorld
import org.bukkit.Location

class Island(
    private val islandGroup: IslandGroup,
    val islandId: Int,
    val world: IslandWorld,
    val spawnLocation: Location
) {

    val center: Location by lazy { getNextIslandLocation(islandGroup.locationId, world) }
}
