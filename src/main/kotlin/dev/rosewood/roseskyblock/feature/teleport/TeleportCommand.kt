package dev.rosewood.roseskyblock.feature.teleport

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import cloud.commandframework.bukkit.parsers.WorldArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        val worldArgument = WorldArgument.of<CommandSender>("world")
        manager.command(builder.literal("teleport", "tp", "go", "home")
            .argument(worldArgument)
            .senderType(Player::class.java)
            .handler { context ->
                // TODO: Temporary command
                val player = context.sender as Player
                val world = context.get(worldArgument)
                val location = player.location.clone()
                location.world = world
                player.teleport(location)
            })
    }

}
