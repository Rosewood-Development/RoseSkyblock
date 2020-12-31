package dev.rosewood.roseskyblock.command

import cloud.commandframework.captions.Caption
import java.util.Collections
import java.util.LinkedList

object SkyblockCaptionKeys {

    val RECOGNIZED_CAPTIONS = LinkedList<Caption>()

    /**
     * Variables: {input}
     */
    val ARGUMENT_PARSE_FAILURE_ISLAND_WORLD_GROUP = of("argument.parse.failure.island_world_group")

    /**
     * Variables: {input}
     */
    val ARGUMENT_PARSE_FAILURE_ISLAND_SCHEMATIC = of("argument.parse.failure.island_schematic")

    private fun of(key: String): Caption {
        val caption = Caption.of(key)
        RECOGNIZED_CAPTIONS += (caption)
        return caption
    }

    /**
     * Get an immutable collection containing all standard caption keys
     *
     * @return Immutable collection of keys
     */
    //fun getSkyblockCaptionKeys(): Collection<Caption> = RECOGNIZED_CAPTIONS.toList()
}
