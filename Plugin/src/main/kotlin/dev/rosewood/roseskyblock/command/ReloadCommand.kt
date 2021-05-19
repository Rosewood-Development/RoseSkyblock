package dev.rosewood.roseskyblock.command

import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.handler.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import org.bukkit.command.CommandSender

class ReloadCommand : SkyblockCommand("reload") {

    override fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>) {
        plugin.reload()
        plugin.getManager(LocaleManager::class.java).sendMessage(sender, "command-reload-reloaded")
    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

}