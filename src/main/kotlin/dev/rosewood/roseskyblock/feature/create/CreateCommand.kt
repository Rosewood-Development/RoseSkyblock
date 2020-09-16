package dev.rosewood.roseskyblock.feature.create

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument
import dev.rosewood.roseskyblock.command.argument.AccessibleIslandsArgument
import dev.rosewood.roseskyblock.command.argument.IslandWorldGroupArgument
import dev.rosewood.roseskyblock.manager.IslandManager
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.world.IslandSchematic
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import org.bukkit.entity.Player

class CreateCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val name: String
        get() = "create"
    override val aliases: Collection<String>
        get() = listOf("new")
    override val arguments: List<SkyblockCommandArgument>
        get() = listOf(
            SkyblockCommandArgument("world group", IslandWorldGroupArgument(this.rosePlugin), false),
            SkyblockCommandArgument("schematic", AccessibleIslandsArgument(this.rosePlugin), false)
        )

    override val executePlayer: (player: Player, args: Array<Any>) -> Unit
        get() = { player, args ->
            val worldGroup = args[1] as IslandWorldGroup
            val schematic = args[2] as IslandSchematic

            val islandManager = this.rosePlugin.getManager(IslandManager::class)

            // TODO: Check if player has island

            val pasteLocation = islandManager.getNextIslandLocation(worldGroup.startingWorld)
            schematic.paste(this.rosePlugin, pasteLocation)
            player.teleport(pasteLocation)

            // TODO: IslandManager#createNewIsland
        }

}
