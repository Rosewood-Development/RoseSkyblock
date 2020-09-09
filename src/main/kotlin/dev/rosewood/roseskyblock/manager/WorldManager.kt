package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.CommentedFileConfiguration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.world.IslandWorld
import java.io.File
import org.bukkit.World

class WorldManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val worlds: MutableMap<String, IslandWorld> = mutableMapOf()

    override fun reload() {
        val worldsFile = File(this.rosePlugin.dataFolder, "worlds.yml")
        val exists = worldsFile.exists()

        val worldsConfig = CommentedFileConfiguration.loadConfiguration(worldsFile)
        if (!exists)
            this.saveDefaults(worldsConfig)


    }

    override fun disable() {
        this.worlds.clear()
    }

    private fun saveDefaults(config: CommentedFileConfiguration) {

    }

}
