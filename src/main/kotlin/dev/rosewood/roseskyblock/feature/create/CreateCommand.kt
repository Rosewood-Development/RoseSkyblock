package dev.rosewood.roseskyblock.feature.create

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument
import dev.rosewood.roseskyblock.command.argument.IslandSchematicArgument
import dev.rosewood.roseskyblock.command.argument.IslandWorldGroupArgument
import dev.rosewood.roseskyblock.util.getNextIslandLocation
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
            SkyblockCommandArgument(IslandWorldGroupArgument(this.rosePlugin), false),
            SkyblockCommandArgument(IslandSchematicArgument(this.rosePlugin), false)
        )

    override val executePlayer: (player: Player, args: Array<Any>) -> Unit
        get() = { player, args ->
            val worldGroup = args[1] as IslandWorldGroup
            val schematic = args[2] as IslandSchematic

            // TODO: Check if player has island

            val pasteLocation = getNextIslandLocation(0, worldGroup.startingWorld)
            schematic.paste(this.rosePlugin, pasteLocation)
            player.teleport(pasteLocation)

            // TODO: IslandManager#createNewIsland
        }

}
