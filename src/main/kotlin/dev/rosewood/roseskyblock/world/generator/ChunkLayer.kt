package dev.rosewood.roseskyblock.world.generator

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator

class ChunkLayer(private val startLayer: Int, private val endLayer: Int, private val material: Material) {

    constructor(layer: Int, material: Material) : this(layer, layer, material)

    fun fill(chunkData: ChunkGenerator.ChunkData) = chunkData.setRegion(0, this.startLayer, 0, 15, this.endLayer, 15, this.material)

}
