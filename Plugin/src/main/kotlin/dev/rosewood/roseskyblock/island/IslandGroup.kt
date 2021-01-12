package dev.rosewood.roseskyblock.island

import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused") // we will remove these later
class IslandGroup(
    val worldGroup: IslandWorldGroup,
    val groupId: Int,
    val ownerUniqueId: UUID,
    val locationId: Int,
    islands: List<Island>,
    members: Map<UUID, IslandMemberLevel>
) {

    val islands = mutableListOf<Island>().apply { addAll(islands) }

    val members = mutableMapOf<UUID, IslandMemberLevel>().apply { putAll(members) }

    fun unlockIsland(world: IslandWorld) {
        // TODO Make this thingy do thingys
    }
}