package dev.rosewood.roseskyblock.command

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import org.bukkit.command.CommandSender

abstract class SkyblockCommand(protected val rosePlugin: RosePlugin) {

    abstract fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>)
}
