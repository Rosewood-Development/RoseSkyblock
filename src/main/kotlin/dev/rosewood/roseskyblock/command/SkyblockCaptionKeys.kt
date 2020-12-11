package dev.rosewood.roseskyblock.command

import cloud.commandframework.captions.Caption
import java.util.Collections
import java.util.LinkedList

class SkyblockCaptionKeys {

    companion object {

        private val RECOGNIZED_CAPTIONS: MutableCollection<Caption> = LinkedList()

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
            this.RECOGNIZED_CAPTIONS.add(caption)
            return caption
        }

        /**
         * Get an immutable collection containing all standard caption keys
         *
         * @return Immutable collection of keys
         */
        fun getSkyblockCaptionKeys(): Collection<Caption> {
            return Collections.unmodifiableCollection(this.RECOGNIZED_CAPTIONS)
        }

    }

}
