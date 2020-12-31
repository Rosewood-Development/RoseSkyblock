package dev.rosewood.roseskyblock.world

import org.bukkit.GameMode

class IslandWorldGroup(
    val name: String,
    val displayName: String,
    val startingWorld: IslandWorld,
    val gamemode: GameMode,
    val worlds: List<IslandWorld>
)
