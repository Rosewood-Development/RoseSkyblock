package dev.rosewood.roseskyblock.island;

import dev.rosewood.roseskyblock.world.IslandWorld;
import dev.rosewood.roseskyblock.world.IslandWorldGroup;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record IslandGroup(
        IslandWorldGroup worldGroup,
        int groupId,
        UUID ownerUniqueId,
        int locationId,
        List<Island> islands,
        Map<UUID, IslandMemberLevel> members
) {

    public void unlockIsland(IslandWorld world) {
        // TODO make this thingy do the thingys
    }

}
