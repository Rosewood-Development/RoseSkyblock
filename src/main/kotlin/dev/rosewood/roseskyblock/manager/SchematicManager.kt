package dev.rosewood.roseskyblock.manager

import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.CommentedFileConfiguration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.util.copyResourceTo
import dev.rosewood.roseskyblock.util.parseEnum
import dev.rosewood.roseskyblock.world.IslandSchematic
import java.io.File
import org.bukkit.Material

class SchematicManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val _schematics = mutableMapOf<String, IslandSchematic>()
    val schematics: Map<String, IslandSchematic>
        get() = this._schematics.toMap()

    override fun reload() {
        val schematicFolder = File(this.rosePlugin.dataFolder, "schematics")
        val schematicsFile = File(this.rosePlugin.dataFolder, "schematics.yml")

        val exists = schematicsFile.exists()
        val schematicsConfig = CommentedFileConfiguration.loadConfiguration(schematicsFile)
        if (!exists) {
            this.rosePlugin.copyResourceTo("default.schem", File(schematicFolder, "default.schem"))
            this.saveDefaults(schematicsConfig)
        }

        val schematicFiles = schematicFolder.listFiles() ?: error("Schematics directory does not exist")
        schematicsConfig.getKeys(false).forEach { schematicName ->
            try {
                val file = schematicFiles.find { it.nameWithoutExtension.equals(schematicName, true) }
                if (file != null) {
                    if (ClipboardFormats.findByFile(file) != null) {
                        val section = schematicsConfig.getConfigurationSection(schematicName) ?: error(schematicName)
                        val displayName = section.getString("name") ?: error("$schematicName.name")
                        val icon = parseEnum(Material::class, section.getString("icon") ?: error("$schematicName.icon"))
                        val lore = section.getStringList("lore")
                        this._schematics[schematicName.toLowerCase()] = IslandSchematic(schematicName, file, displayName, icon, lore)
                    } else {
                        this.rosePlugin.logger.warning("File located in the schematics folder is not a valid schematic: ${file.name}")
                    }
                } else {
                    this.rosePlugin.logger.warning("Unable to locate a schematic that was listed in the schematics.yml file: $schematicName")
                }
            } catch (ex: Exception) {
                this.rosePlugin.logger.severe("Missing schematics.yml section: ${ex.message}")
            }
        }
    }

    override fun disable() {
        this._schematics.clear()
    }

    private fun saveDefaults(config: CommentedFileConfiguration) {
        val section = config.createSection("default")
        section["name"] = "&eDefault Island"
        section["icon"] = Material.GRASS_BLOCK.name
        section["lore"] = listOf(
            "&7The default schematic for RoseSkyblock.",
            "&7Handle with care."
        )
        config.save()
    }

    fun getSchematic(name: String): IslandSchematic? {
        for (schematic in this.schematics.values)
            if (schematic.name.equals(name, ignoreCase = true))
                return schematic
        return null
    }

}
