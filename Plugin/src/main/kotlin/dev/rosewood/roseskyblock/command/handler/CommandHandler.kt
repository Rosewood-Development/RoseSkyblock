package dev.rosewood.roseskyblock.command.handler

import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.manager.LocaleManager
import kotlin.streams.toList
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.permissions.Permissible
import org.bukkit.util.StringUtil


/**
 * Abstract class to handle the majority of the logic dealing with commands.
 * Allows for a nested structure of commands.
 */
abstract class CommandHandler(private val plugin: RoseSkyblock, val cmd: String) : TabExecutor, NamedExecutor {

    // Registered commands for the handler.
    private val registeredCommands = mutableMapOf<String, SkyblockCommand>()

    // Registered sub commands and the command's handler.
    private val registeredHandlers = mutableMapOf<String, CommandHandler>()

    /**
     * Register a command with an execution handler.
     *
     * @param label   - Command to listen for.
     * @param command - Execution handler that will handle the logic behind the
     * command.
     */
    fun registerCommand(label: String, command: SkyblockCommand) {

        if (this.registeredCommands.containsKey(label)) {
            this.plugin.logger.warning("Replacing existing command for: $label")

        }

        this.registeredCommands[label] = command
    }

    /**
     * Unregister a command for this handler.
     *
     * @param label - Command to stop handling.
     */
    fun unregisterCommand(label: String) {
        this.registeredCommands.remove(label)
    }

    /**
     * Register a subcommand with a command handler.
     *
     * @param handler - Command handler.
     */
    fun registerHandler(handler: CommandHandler) {

        if (this.registeredHandlers.containsKey(handler.cmd)) {
            this.plugin.logger.warning("Replacing existing handler for: " + handler.cmd)
        }

        this.registeredHandlers[handler.cmd] = handler
    }

    /**
     * Unregister a subcommand.
     *
     * @param label - Subcommand to remove.
     */
    fun unregisterHandler(label: String) {
        this.registeredHandlers.remove(label)
    }

    /**
     * Command loop that will go through the linked handlers until it finds the
     * appropriate handler or command execution handler to do the logic for.
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        // Define locale manager variable
        val locale = this.plugin.getManager(LocaleManager::class.java)

        if (args.isEmpty()) {
            this.noArgs(sender)
            return true
        }

        val subcmd = args[0].toLowerCase()

        // Check known handlers first and pass to them
        val handler = this.registeredHandlers[subcmd]
        if (handler != null) {

            // Make sure they have permission
            if (!handler.hasPermission(sender)) {
                this.plugin.getManager(LocaleManager::class.java).sendMessage(sender, "no-permission")
                return true
            }

            handler.onCommand(sender, command, label, this.shortenArgs(args))
            return true
        }

        // Its our command, so handle it if its registered.
        val subCommand = this.registeredCommands[subcmd]
        if (subCommand == null) {
            this.unknownCommand(sender, args)
            return true
        }

        // Make sure they have permission
        if (!subCommand.hasPermission(sender)) {
            locale.sendMessage(sender, "no-permission")
            return true
        }

        // Execute command
        try {
            subCommand.execute(this.plugin, sender, this.shortenArgs(args))
        } catch (e: ArrayIndexOutOfBoundsException) {
            sender.sendMessage(ChatColor.RED.toString() + "A RoseSkyblock error occurred while executing that command. Did you enter an invalid parameter?")
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {

        if (args.isEmpty()) return emptyList()

        val subcmd = args[0].toLowerCase()

        if (args.size == 1) {

            // Complete against command names the sender has permission for

            val commandNames = mutableListOf<String>()
            commandNames.addAll(

                this.registeredHandlers.entries.stream()
                    .filter { (_, value) -> value.hasPermission(sender) }
                    .map { it.key }
                    .toList()

            )

            commandNames.addAll(
                this.registeredCommands.entries.stream()
                    .filter { (_, value) -> value.hasPermission(sender) }
                    .map { it.key }
                    .toList()
            )

            val completions = mutableListOf<String>()
            StringUtil.copyPartialMatches(subcmd, commandNames, completions)
            return completions
        }

        // Try to find a handler to pass to
        val handler = this.registeredHandlers[subcmd]
        if (handler != null && handler.hasPermission(sender)) return handler.onTabComplete(sender, command, alias, this.shortenArgs(args))

        // Look for a command to pass to
        val subCommand = this.registeredCommands[subcmd]
        return if (subCommand != null && subCommand.hasPermission(sender)) subCommand.tabComplete(plugin, sender, this.shortenArgs(args)) else emptyList()

        // No matching commands, return an empty list
    }

    /**
     * Method that is called on a CommandHandler if there is no additional
     * arguments given that specify a specific command.
     *
     * @param sender  - Sender of the command.
     */
    abstract fun noArgs(sender: CommandSender)

    /**
     * Allow for the command handler to have special logic for unknown commands.
     * Useful for when expecting a player name parameter on a root command
     * handler command.
     *
     * @param sender  - Sender of the command.
     * @param args    - Arguments.
     */
    abstract fun unknownCommand(sender: CommandSender, args: Array<String>)

    /**
     * @return a combination of all executable commands and handlers sorted by name
     */
    val executables: List<NamedExecutor>
        get() {

            val executors: MutableList<NamedExecutor> = ArrayList()
            executors.addAll(this.registeredHandlers.values)
            executors.addAll(this.registeredCommands.values)
            executors.sortBy { it.name() }

            return executors

        }

    override fun hasPermission(permissible: Permissible): Boolean {
        return true
    }

    /**
     * Shortens the given string array by removing the first entry.
     *
     * @param args - Array to shorten.
     * @return Shortened array.
     */
    private fun shortenArgs(args: Array<String>): Array<String> {

        if (args.isEmpty()) {
            return args
        }

        val argList = mutableListOf(args).subList(1, args.size)
        return argList.toTypedArray().firstOrNull() ?: args
    }

}