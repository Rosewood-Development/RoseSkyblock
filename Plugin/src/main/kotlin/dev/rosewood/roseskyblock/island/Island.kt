package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.nms.BorderColor
import dev.rosewood.roseskyblock.util.getNextIslandLocation
import dev.rosewood.roseskyblock.world.IslandWorld
import org.bukkit.Location

data class Island(
    val islandGroup: IslandGroup,
    val islandId: Int,
    val world: IslandWorld,
    val spawnLocation: Location,
) {

    @Suppress("unused") // we will use this later
    val center: Location by lazy { getNextIslandLocation(this.islandGroup.locationId, this.world) }
}
