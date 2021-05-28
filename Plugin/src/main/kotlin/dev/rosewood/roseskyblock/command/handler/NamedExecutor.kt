package dev.rosewood.roseskyblock.command.handler

import org.bukkit.permissions.Permissible

interface NamedExecutor {

    /**
     * @return The name of the executor.
     */
    fun name(): String

    /**
     * Checks if a permissible has permission to use the command.
     *
     * @param permissible The permissible.
     * @return true if the command can be run.
     */
    fun hasPermission(permissible: Permissible): Boolean
}
