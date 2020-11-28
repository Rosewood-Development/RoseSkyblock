package dev.rosewood.roseskyblock.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.rosewood.rosegarden.RosePlugin
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

abstract class SkyblockCommand(protected val rosePlugin: RosePlugin) {

    private var registered = false

    protected abstract val name: String
    protected abstract val aliases: Collection<String>
    protected abstract val arguments: List<SkyblockCommandArgument>

    protected open val prependedArguments: List<SkyblockCommandArgument> = listOf()

    protected open val executePlayer: ((player: Player, args: Array<Any>) -> Unit)? = null
    protected open val executeConsole: ((console: ConsoleCommandSender, args: Array<Any>) -> Unit)? = null
    protected open val executeAny: ((sender: CommandSender, args: Array<Any>) -> Unit)? = null

    open fun register() {
        if (this.registered)
            error("Command ${this.name} has already been registered")

        if (this.executePlayer == null && this.executeConsole == null && this.executeAny == null)
            error("Tried to register a command with no executor")

        val names = listOf(this.name, *this.aliases.toTypedArray())
        val commandArgs = mutableListOf<Argument>()
        val args = mutableListOf<SkyblockCommandArgument>()

        args.addAll(this.prependedArguments)
        args.add(SkyblockCommandArgument(MultiLiteralArgument(*names.toTypedArray()), false))
        args.addAll(this.arguments)

        var previousOptional = false
        for (arg in args) {
            if (!arg.optional && previousOptional)
                error("Cannot provide a required argument after an optional argument")

            if (arg.optional)
                this.registerCommands(commandArgs)

            commandArgs.add(arg.argument)
            previousOptional = arg.optional
        }

        this.registerCommands(commandArgs)

        this.registered = true
    }

    private fun registerCommands(arguments: List<Argument>) {
        // A bit of a hack to get aliases to work properly
        // This is probably polluting the command map, but it works for now
        listOf("skyblock", "sb", "is", "island", "rsb", "roseskyblock").forEach { commandName ->
            val command = CommandAPICommand(commandName)
                .withArguments(arguments)

            if (this.executePlayer != null)
                command.executesPlayer(this.executePlayer!!)

            if (this.executeConsole != null)
                command.executesConsole(this.executeConsole!!)

            if (this.executeAny != null)
                command.executes(this.executeAny!!)

            command.register()
        }
    }

}
