package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.util.getNextIslandLocation
import dev.rosewood.roseskyblock.world.IslandWorld
import org.bukkit.Location
import java.util.UUID

data class Island(
    val islandGroup: IslandGroup,
    val islandId: UUID,
    val world: IslandWorld,
    val spawnLocation: Location,
//    var settings: IslandSettings
) {

    @Suppress("unused") // we will use this later
    val center: Location by lazy { getNextIslandLocation(this.islandGroup.locationId, this.world) }
}
