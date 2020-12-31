package dev.rosewood.roseskyblock.world

import dev.rosewood.roseskyblock.world.generator.ChunkLayer
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.block.Biome

class IslandWorld(
    val worldName: String,
    val displayName: String,
    val environment: World.Environment,
    val biome: Biome,
    val chunkLayers: List<ChunkLayer>,
    val portalLinks: PortalLinks,
    val islandHeight: Int
) {

    val world: World
        get() = Bukkit.getWorld(worldName) ?: error("World ${this.worldName} is not loaded")
}
