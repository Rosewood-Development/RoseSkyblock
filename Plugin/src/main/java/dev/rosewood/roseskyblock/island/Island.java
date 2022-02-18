package dev.rosewood.roseskyblock.island;

import dev.rosewood.roseskyblock.util.SkyblockUtil;
import dev.rosewood.roseskyblock.world.IslandWorld;
import org.bukkit.Location;

public record Island(
        IslandGroup islandGroup,
        int islandId,
        IslandWorld world,
        Location spawnLocation
) {

    public Location getCenter() {
        return SkyblockUtil.getNextIslandLocation(this.islandGroup.locationId(), this.world);
    }
}
