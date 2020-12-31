package dev.rosewood.roseskyblock.feature.default

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.command.CommandSender

class DefaultCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        builder.handler { context ->
            val localeManager = rosePlugin.getManager<LocaleManager>()
            val baseColor: String = localeManager.getLocaleMessage("base-command-color")
            localeManager.sendCustomMessage(
                context.sender,
                baseColor + "Running <g:#8A2387:#E94057:#F27121>RoseSkyblock" + baseColor + " v" + this.rosePlugin.description.version
            )
            localeManager.sendCustomMessage(
                context.sender,
                baseColor + "Plugin created by: <g:#41e0f0:#ff8dce>" + this.rosePlugin.description.authors[0]
            )
            localeManager.sendSimpleMessage(context.sender, "base-command-help")
        }
    }

}
