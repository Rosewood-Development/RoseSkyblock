package dev.rosewood.roseskyblock.island.feature.create

import dev.jorel.commandapi.arguments.StringArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.island.feature.SkyblockCommand
import dev.rosewood.roseskyblock.island.feature.SkyblockCommandArgument
import dev.rosewood.roseskyblock.manager.IslandManager
import dev.rosewood.roseskyblock.manager.SchematicManager
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.entity.Player

class CreateCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val name: String
        get() = "create"
    override val aliases: Collection<String>
        get() = listOf("new")
    override val arguments: List<SkyblockCommandArgument>
        get() = listOf(SkyblockCommandArgument("world", StringArgument(), false))

    override val executePlayer: (player: Player, args: Array<Any>) -> Unit
        get() = { player, args ->
            val world = this.rosePlugin.getManager(WorldManager::class).getIslandWorld(args[1] as String)
            if (world != null) {
                val pasteLocation = this.rosePlugin.getManager(IslandManager::class).getNextIslandLocation(world)
                this.rosePlugin.getManager(SchematicManager::class).schematics.values.first().paste(this.rosePlugin, pasteLocation)
                player.teleport(pasteLocation.clone().add(0.0, 1.0, 0.0))
            } else {
                player.sendMessage("Invalid world: ${args[1]}")
            }
        }

}
