package dev.rosewood.roseskyblock.feature.teleport

import dev.jorel.commandapi.arguments.StringArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TeleportCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val name: String
        get() = "teleport"
    override val aliases: Collection<String>
        get() = listOf("tp", "go", "home")
    override val arguments: List<SkyblockCommandArgument>
        get() = listOf(SkyblockCommandArgument("world", StringArgument(), true))

    override val executePlayer: (player: Player, args: Array<Any>) -> Unit
        get() = { player, args ->
            // TODO: Temporary command
            if (args.size == 1) {
                player.sendMessage("Please provide a world name to teleport to")
            } else {
                val world = Bukkit.getWorld(args[1] as String)
                if (world != null) {
                    val location = player.location.clone()
                    location.world = world
                    player.teleport(location)
                } else {
                    player.sendMessage("That world doesn't exist")
                }
            }
        }

}
