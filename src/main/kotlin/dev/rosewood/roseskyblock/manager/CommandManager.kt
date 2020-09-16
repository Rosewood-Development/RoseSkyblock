package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.feature.admin.reload.ReloadAdminCommand
import dev.rosewood.roseskyblock.feature.create.CreateCommand
import dev.rosewood.roseskyblock.feature.default.DefaultCommand
import dev.rosewood.roseskyblock.feature.help.HelpCommand
import dev.rosewood.roseskyblock.feature.teleport.TeleportCommand

class CommandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    init {
        listOf(
            DefaultCommand(this.rosePlugin),
            HelpCommand(this.rosePlugin),
            CreateCommand(this.rosePlugin),
            TeleportCommand(this.rosePlugin),

            ReloadAdminCommand(this.rosePlugin)
        ).forEach { it.register() }
    }

    override fun reload() {

    }

    override fun disable() {

    }

}
