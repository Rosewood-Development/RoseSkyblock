package dev.rosewood.roseskyblock.command.handler

import dev.rosewood.rosegarden.utils.StringPlaceholders
import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.BorderCommand
import dev.rosewood.roseskyblock.command.CreateCommand
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

        // TODO Send help command
    }

    override fun unknownCommand(sender: CommandSender, args: Array<String>) {
        this.locale.sendMessage(sender, "unknown-command", StringPlaceholders.single("input", args[0]))
    }

    override fun name(): String {
        return this.cmd
    }

    init {
        // Register Plugin Commands.
        this.registerCommand("create", CreateCommand())
        this.registerCommand("border", BorderCommand())

    }

}