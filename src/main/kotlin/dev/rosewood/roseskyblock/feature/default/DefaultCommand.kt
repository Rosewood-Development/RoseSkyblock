package dev.rosewood.roseskyblock.feature.default

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.util.getManager

class DefaultCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    // Unused
    override val name: String get() = ""
    override val aliases: Collection<String> get() = listOf()
    override val arguments: List<SkyblockCommandArgument> get() = listOf()

    override fun register() {
        CommandAPICommand("skyblock")
            .withAliases("sb", "is", "island", "rsb", "roseskyblock")
            .executes(CommandExecutor { sender, _ ->
                val localeManager = this.rosePlugin.getManager(LocaleManager::class)
                val baseColor: String = localeManager.getLocaleMessage("base-command-color")
                localeManager.sendCustomMessage(
                    sender,
                    baseColor + "Running <g:#8A2387:#E94057:#F27121>RoseSkyblock" + baseColor + " v" + this.rosePlugin.description.version
                )
                localeManager.sendCustomMessage(
                    sender,
                    baseColor + "Plugin created by: <g:#41e0f0:#ff8dce>" + this.rosePlugin.description.authors[0]
                )
                localeManager.sendSimpleMessage(sender, "base-command-help")
            }).register()
    }

}
