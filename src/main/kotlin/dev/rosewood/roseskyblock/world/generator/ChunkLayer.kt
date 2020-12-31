package dev.rosewood.roseskyblock.world.generator

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator

class ChunkLayer(val startLayer: Int, val endLayer: Int, val material: Material) {

    constructor(layer: Int, material: Material) : this(layer, layer, material)

    fun fill(chunkData: ChunkGenerator.ChunkData) = chunkData.setRegion(0, this.startLayer, 0, 16, this.endLayer + 1, 16, this.material)
}
