package dev.rosewood.roseskyblock.command.handler

import dev.rosewood.rosegarden.utils.StringPlaceholders
import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.BorderCommand
import dev.rosewood.roseskyblock.command.CreateCommand
import dev.rosewood.roseskyblock.command.ReloadCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import org.bukkit.command.CommandSender

/**
 * Handles the commands for the root command.
 *
 * @author Mitsugaru
 */
class Commander(private val plugin: RoseSkyblock) : CommandHandler(plugin, "island") {

    private val locale = this.plugin.getManager(LocaleManager::class.java)

    override fun noArgs(sender: CommandSender) {

        val baseColor = this.locale.getLocaleMessage("base-command-color")
        this.locale.sendMessage(sender, "base-command-color")
        this.locale.sendCustomMessage(sender, baseColor + "Running <g:#8A2387:#E94057:#F27121>RoseSkyblock" + baseColor + this.plugin.description.version)
        this.locale.sendCustomMessage(sender, baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + this.plugin.description.authors[0])
        this.locale.sendSimpleMessage(sender, "base-command-help")
    }

    override fun unknownCommand(sender: CommandSender, args: Array<String>) {
        this.locale.sendMessage(sender, "unknown-command", StringPlaceholders.single("input", args[0]))
    }

    override fun name(): String {
        return this.cmd
    }

    init {
        // Register Plugin Commands.
        this.registerCommand("border", BorderCommand())
        this.registerCommand("create", CreateCommand())
        this.registerCommand("reload", ReloadCommand())

    }

}