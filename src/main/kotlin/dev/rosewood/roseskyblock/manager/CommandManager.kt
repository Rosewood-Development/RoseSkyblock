package dev.rosewood.roseskyblock.manager

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.extension.getManager
import net.md_5.bungee.api.ChatColor

class CommandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    init {
        val localeManager = this.rosePlugin.getManager(LocaleManager::class)

        // Base Command
        CommandAPICommand("skyblock")
            .withAliases("sb", "is", "island", "rsb", "roseskyblock")
            .executes(CommandExecutor { sender, _ ->
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

        // Help Command
        val helpArgs: LinkedHashMap<String, Argument> = LinkedHashMap()
        helpArgs["help"] = LiteralArgument("help")

        CommandAPICommand("skyblock")
            .withAliases("sb", "is", "island", "rsb", "roseskyblock")
            .withArguments(helpArgs)
            .executes(CommandExecutor { sender, _ ->
                localeManager.sendMessage(sender, "command-help-title")
                localeManager.sendSimpleMessage(sender, "command-help-description")
                localeManager.sendSimpleMessage(sender, "command-reload-description")
            }).register()

        // Reload Command
        val reloadArgs: LinkedHashMap<String, Argument> = LinkedHashMap()
        reloadArgs["reload"] = LiteralArgument("reload")

        CommandAPICommand("skyblock")
            .withAliases("sb", "is", "island", "rsb", "roseskyblock")
            .withArguments(reloadArgs)
            .executes(CommandExecutor { sender, _ ->
                this.rosePlugin.reload()
                localeManager.sendMessage(sender, "command-reload-reloaded")
            }).register()

        // Create Command
        val createArgs: LinkedHashMap<String, Argument> = LinkedHashMap()
        createArgs["create"] = MultiLiteralArgument("create", "new")

        CommandAPICommand("skyblock")
            .withAliases("sb", "is", "island", "rsb", "roseskyblock")
            .withArguments(createArgs)
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                val pasteLocation = this.rosePlugin.getManager(IslandManager::class).getNextIslandLocation(player.world)
                pasteLocation.y = 0.0
                this.rosePlugin.getManager(SchematicManager::class).schematics.values.first().paste(this.rosePlugin, player.location.clone().add(pasteLocation))
                player.sendMessage(ChatColor.of("#c763a9").toString() + "Pasting schematic!")
            }).register()
    }

    override fun reload() {

    }

    override fun disable() {

    }

}
