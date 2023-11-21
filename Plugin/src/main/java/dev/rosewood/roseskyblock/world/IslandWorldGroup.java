package dev.rosewood.roseskyblock.world;

import org.bukkit.GameMode;

import java.util.List;

public record IslandWorldGroup(
        String name,
        String displayName,
        IslandWorld startingWorld,
        GameMode gamemode,
        List<IslandWorld> worlds
) {

    // TODO: Add some thingys here.
}
