package dev.rosewood.roseskyblock.world

import org.bukkit.GameMode

data class IslandWorldGroup(
    val name: String,
    val displayName: String,
    val startingWorld: IslandWorld,
    val gamemode: GameMode,
    val worlds: List<IslandWorld>
) {
    // TODO: Add some thingys here
}
