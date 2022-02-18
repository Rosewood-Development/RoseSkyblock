package dev.rosewood.roseskyblock.world.generator;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class LayeredChunkGenerator extends ChunkGenerator {

    private final List<ChunkLayer> layers;
    private final Biome biome;

    public LayeredChunkGenerator(List<ChunkLayer> layers, Biome biome) {
        this.layers = layers;
        this.biome = biome;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid grid) {
        ChunkData data = this.createChunkData(world);
        this.layers.forEach(chunkLayer -> chunkLayer.fill(data));

        for (int x = 0; x < 16; x += 4)
            for (int z = 0; z < 16; z += 4)
                for (int y = 0; y < world.getMaxHeight() + 1; y += 4)
                    grid.setBiome(x, y, z, this.biome);

        return data;
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }
}
