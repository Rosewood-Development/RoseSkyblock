package dev.rosewood.roseskyblock.command.argument

import dev.jorel.commandapi.arguments.CustomArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.world.IslandWorldGroup

class IslandWorldGroupArgument(rosePlugin: RosePlugin) : CustomArgument<IslandWorldGroup>("worldGroup", { input ->
    val worldManager = rosePlugin.getManager(WorldManager::class)

    worldManager.worldGroups.find { it.name.equals(input, true) }
        ?: throw CustomArgumentException(MessageBuilder("Unknown world group: ").appendArgInput())
}) {

    init {
        val worldManager = rosePlugin.getManager(WorldManager::class)
        this.overrideSuggestions { sender, args ->
            // TODO: World group permissions
            worldManager.worldGroups.map(IslandWorldGroup::name).toTypedArray()
        }
    }

}
