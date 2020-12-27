package dev.rosewood.roseskyblock.feature.create

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.argument.IslandSchematicArgument
import dev.rosewood.roseskyblock.command.argument.IslandWorldGroupArgument
import dev.rosewood.roseskyblock.util.getNextIslandLocation
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        val worldGroupArgument = IslandWorldGroupArgument.of<CommandSender>("worldGroup")
        val schematicArgument = IslandSchematicArgument.of<CommandSender>("schematic")
        manager.command(builder.literal("create", "new")
            .argument(worldGroupArgument)
            .argument(schematicArgument)
            .senderType(Player::class.java)
            .handler { context ->
                val player = context.sender as Player
                val worldGroup = context.get(worldGroupArgument)
                val schematic = context.get(schematicArgument)

                //this.rosePlugin.getManager(DataManager::class).hasIsland(player)

                val pasteLocation = getNextIslandLocation(0, worldGroup.startingWorld)
                schematic.paste(this.rosePlugin, pasteLocation)
                player.teleport(pasteLocation)

                // TODO: IslandManager#createNewIsland
            })
    }

}
