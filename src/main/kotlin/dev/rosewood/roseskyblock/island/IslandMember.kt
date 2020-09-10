package dev.rosewood.roseskyblock.island

import java.util.UUID
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class IslandMember(val playerUniqueId: UUID) {

    val islandMemberLevels: MutableMap<Island, IslandMemberLevel> = mutableMapOf()

    val player: Player
        get() = Bukkit.getPlayer(this.playerUniqueId) ?: error("Player is not online")

}
