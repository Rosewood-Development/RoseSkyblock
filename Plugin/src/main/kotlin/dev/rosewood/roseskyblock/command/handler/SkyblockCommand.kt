package dev.rosewood.roseskyblock.command.handler

import dev.rosewood.roseskyblock.RoseSkyblock
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permissible

abstract class SkyblockCommand(val name: String) : NamedExecutor {

    /**
     * Execute a method when the command is ran.
     *
     * @param plugin - RoseSkyblock Instance.
     * @param sender - The command sender.
     * @param args - The command arguments.
     */
    abstract fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>)

    /**
     * The tab complete method for the command.
     *
     * @param plugin - RoseSkyblock Instance.
     * @param sender - The command sender.
     * @param args - The command arguments.
     */
    abstract fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): List<String>

    override fun name(): String {
        return this.name
    }

    override fun hasPermission(permissible: Permissible): Boolean {
        return permissible.hasPermission("roseskyblock." + this.name)
    }

}
