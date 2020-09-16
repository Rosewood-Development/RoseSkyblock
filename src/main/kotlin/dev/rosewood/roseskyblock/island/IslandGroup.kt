package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.UUID

class IslandGroup(
    val worldGroup: IslandWorldGroup,
    val groupId: Int,
    val ownerUniqueId: UUID,
    val locationId: Int,
    islands: List<Island>,
    members: Map<UUID, IslandMemberLevel>
) {

    private val _islands: MutableList<Island> = mutableListOf()
    val islands: List<Island>
        get() = this._islands.toList()

    private val _members: MutableMap<UUID, IslandMemberLevel> = mutableMapOf()
    val members: Map<UUID, IslandMemberLevel>
        get() = this._members.toMap()

    init {
        this._islands.addAll(islands)
        this._members.putAll(members)
    }

    fun unlockIsland(world: IslandWorld) {

    }

}
