package dev.rosewood.roseskyblock.world;

import dev.rosewood.roseskyblock.world.generator.ChunkLayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.List;

public record IslandWorld(
        String worldName,
        String displayName,
        World.Environment enviroment,
        Biome biome,
        List<ChunkLayer> layers,
        PortalLinks links,
        int islandHeight
) {

    public World getWorld() {
        World world = Bukkit.getWorld(this.worldName);

        if (world == null)
            throw new NullPointerException("World " + this.worldName + " is not loaded.");

        return world;
    }
}
