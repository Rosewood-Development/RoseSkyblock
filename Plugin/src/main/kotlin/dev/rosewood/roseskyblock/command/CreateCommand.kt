package dev.rosewood.roseskyblock.command

import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.handler.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.manager.SchematicManager
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.util.getNextIslandLocation
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand : SkyblockCommand("create") {

    override fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>) {
        val locale = plugin.getManager(LocaleManager::class.java)
        val worldManager = plugin.getManager(WorldManager::class.java)

        if (sender !is Player) {
            locale.sendMessage(sender, "misc-player-only")
            return
        }

        val worldGroup = worldManager.worldGroups.find { it.name.equals(args[0], true) } ?: return
        val schematic = plugin.getManager(SchematicManager::class.java).schematics.map { it.value }.find { it.name.equals(args[1], true) } ?: return
        val location = getNextIslandLocation(0, worldGroup.startingWorld)
        schematic.paste(plugin, location)
        sender.teleport(location)

        // TODO, Create island
    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): List<String> {
        val worldGroups = plugin.getManager(WorldManager::class.java).worldGroups
        val schems = plugin.getManager(SchematicManager::class.java).schematics

        return when (args.size) {
            1 -> worldGroups.map { it.name }.toList()
            2 -> schems.map { it.value.name }.toList()
            else -> listOf()
        }
    }

}
