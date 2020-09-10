package dev.rosewood.roseskyblock.island.feature.help

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.island.feature.SkyblockCommand
import dev.rosewood.roseskyblock.island.feature.SkyblockCommandArgument
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.command.CommandSender

class HelpCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val name: String
        get() = "help"
    override val aliases: Collection<String>
        get() = listOf()
    override val arguments: List<SkyblockCommandArgument>
        get() = listOf()

    override val executeAny: (sender: CommandSender, args: Array<Any>) -> Unit
        get() = { sender, _ ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            localeManager.sendMessage(sender, "command-help-title")
            localeManager.sendSimpleMessage(sender, "command-help-description")
            localeManager.sendSimpleMessage(sender, "command-reload-description")
        }

}
