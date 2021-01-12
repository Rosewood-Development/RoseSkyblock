package dev.rosewood.roseskyblock.feature.admin.reload

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.command.CommandSender

class ReloadAdminCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        manager.command(builder.literal("reload").handler { context ->
            val localeManager = this.rosePlugin.getManager<LocaleManager>()
            this.rosePlugin.reload()
            localeManager.sendMessage(context.sender, "command-reload-reloaded")
        })
    }

}
