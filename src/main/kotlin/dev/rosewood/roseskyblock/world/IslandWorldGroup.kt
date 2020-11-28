package dev.rosewood.roseskyblock.world

import org.bukkit.GameMode

class IslandWorldGroup(
    var name: String,
    var displayName: String,
    val startingWorld: IslandWorld,
    var gamemode: GameMode,
    val worlds: List<IslandWorld>
) {

}
