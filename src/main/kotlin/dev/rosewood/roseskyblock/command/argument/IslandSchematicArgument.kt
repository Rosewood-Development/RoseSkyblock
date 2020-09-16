package dev.rosewood.roseskyblock.command.argument

import dev.jorel.commandapi.arguments.CustomArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.manager.SchematicManager
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.world.IslandSchematic
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import org.bukkit.Bukkit

class IslandSchematicArgument(rosePlugin: RosePlugin) : CustomArgument<IslandSchematic>({ input ->
    val schematicManager = rosePlugin.getManager(SchematicManager::class)

    schematicManager.schematics[input.toLowerCase()]
        ?: throw CustomArgumentException(MessageBuilder("Unknown schematic: ").appendArgInput())
}) {

    init {
        val schematicManager = rosePlugin.getManager(SchematicManager::class)
        this.overrideSuggestions { sender, args ->
            // TODO: Only show schematics available for that island group and that the user has permission for
            schematicManager.schematics.keys.map(String::toLowerCase).toTypedArray()
        }
    }

}
