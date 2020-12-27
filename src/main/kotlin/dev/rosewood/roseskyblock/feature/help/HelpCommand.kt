package dev.rosewood.roseskyblock.feature.help

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.command.CommandSender

class HelpCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        manager.command(builder.literal("help").handler { context ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            localeManager.sendMessage(context.sender, "command-help-title")
            localeManager.sendSimpleMessage(context.sender, "command-help-description")
            localeManager.sendSimpleMessage(context.sender, "command-reload-description")
        })
    }

}
