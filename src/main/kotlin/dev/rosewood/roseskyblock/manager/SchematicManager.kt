package dev.rosewood.roseskyblock.manager

import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.world.IslandSchematic
import java.io.File

class SchematicManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    val schematics: MutableMap<String, IslandSchematic> = mutableMapOf()

    override fun reload() {
        val schematicFolder = File(this.rosePlugin.dataFolder, "schematics")
        schematicFolder.mkdirs()

        schematicFolder.listFiles()?.forEach {
            if (ClipboardFormats.findByFile(it) != null) {
                val name = it.nameWithoutExtension
                this.schematics[name] = IslandSchematic(name, it)
            } else {
                this.rosePlugin.logger.warning("File located in the schematics folder is not a valid schematic: ${it.name}")
            }
        }
    }

    override fun disable() {
        this.schematics.clear()
    }

}
