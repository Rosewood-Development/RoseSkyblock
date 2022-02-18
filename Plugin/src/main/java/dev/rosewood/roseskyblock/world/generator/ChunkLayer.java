package dev.rosewood.roseskyblock.world.generator;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;

public record ChunkLayer(int startLayer, int endLayer, Material material) {

    public void fill(ChunkGenerator.ChunkData data) {
        data.setRegion(0, this.startLayer, 0, 16, this.endLayer + 1, 16, this.material);
    }

}
