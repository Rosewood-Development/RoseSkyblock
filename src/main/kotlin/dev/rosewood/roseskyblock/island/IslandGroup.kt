package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.*

class IslandGroup(
    val worldGroup: IslandWorldGroup,
    val groupId: Int,
    val ownerUniqueId: UUID,
    val locationId: Int,
    islands: List<Island>,
    members: Map<UUID, IslandMemberLevel>
) {

    // IJ suggested these two could be private
    private val islands = mutableListOf<Island>()

    private val members = mutableMapOf<UUID, IslandMemberLevel>()

    init {
        this.islands.addAll(islands)
        this.members.putAll(members)
    }

    fun unlockIsland(world: IslandWorld) {
        TODO("Make this thingy do thingys")
    }
}
