package dev.rosewood.roseskyblock.command

import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.handler.SkyblockCommand
import dev.rosewood.roseskyblock.manager.DataManager
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.manager.WorldManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportCommand : SkyblockCommand("teleport") {

    override fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>) {
        val locale = plugin.getManager(LocaleManager::class.java)

        if (sender !is Player) {
            locale.sendMessage(sender, "misc-player-only")
            return
        }

        if (args.isEmpty()) {
            // TOOD, Add message
            return
        }

        val worldGroup = plugin.getManager(WorldManager::class.java).getIslandWorldGroup(args[0]) ?: return
        val islandGroup = plugin.getManager(DataManager::class.java).getIslandGroup(sender, worldGroup) ?: return
        sender.teleport((islandGroup.islands.firstOrNull() ?: return).center)
    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): List<String> {
        val worldGroups = plugin.getManager(WorldManager::class.java).worldGroups

        return when (args.size) {
            1 -> worldGroups.map { it.name }.toList()
            else -> listOf()
        }
    }

}
