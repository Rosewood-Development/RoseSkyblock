package dev.rosewood.roseskyblock.command

import cloud.commandframework.captions.Caption
import java.util.LinkedList

object SkyblockCaptionKeys {

    @Suppress("MemberVisibilityCanBePrivate") // we will use this outside of here
    val RECOGNIZED_CAPTIONS = LinkedList<Caption>()

    /**
     * Variables: {input}
     */
    val ARGUMENT_PARSE_FAILURE_ISLAND_WORLD_GROUP = this.of("argument.parse.failure.island_world_group")

    /**
     * Variables: {input}
     */
    val ARGUMENT_PARSE_FAILURE_ISLAND_SCHEMATIC = this.of("argument.parse.failure.island_schematic")

    private fun of(key: String): Caption {
        val caption = Caption.of(key)
        this.RECOGNIZED_CAPTIONS += caption
        return caption
    }

    /**
     * Get an immutable collection containing all standard caption keys
     * This is here for consistency with the cloud command framework, it is completely unused.
     *
     * @return Immutable collection of keys
     */
    @Suppress("unused")
    fun getSkyblockCaptionKeys(): Collection<Caption> = this.RECOGNIZED_CAPTIONS.toList()

}
