package dev.rosewood.roseskyblock.world.generator;

import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class LayeredChunkGenerator extends ChunkGenerator {

    private final List<ChunkLayer> layers;

    public LayeredChunkGenerator(List<ChunkLayer> layers) {
        this.layers = layers;
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        this.layers.forEach(chunkLayer -> chunkLayer.fill(chunkData));
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        this.layers.forEach(chunkLayer -> chunkLayer.fill(chunkData));
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        this.layers.forEach(chunkLayer -> chunkLayer.fill(chunkData));
    }

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        this.layers.forEach(chunkLayer -> chunkLayer.fill(chunkData));
    }

}
