package dev.rosewood.roseskyblock.feature.admin.reload

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument
import dev.rosewood.roseskyblock.command.SkyblockAdminCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.command.CommandSender

class ReloadAdminCommand(rosePlugin: RosePlugin) : SkyblockAdminCommand(rosePlugin) {

    override val name: String
        get() = "reload"
    override val aliases: Collection<String>
        get() = listOf()
    override val arguments: List<SkyblockCommandArgument>
        get() = listOf()

    override val executeAny: (sender: CommandSender, args: Array<Any>) -> Unit
        get() = { sender, _ ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            this.rosePlugin.reload()
            localeManager.sendMessage(sender, "command-reload-reloaded")
        }

}
