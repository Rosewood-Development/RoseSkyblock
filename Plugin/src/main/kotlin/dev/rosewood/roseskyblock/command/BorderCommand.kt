package dev.rosewood.roseskyblock.command

import dev.rosewood.rosegarden.utils.StringPlaceholders
import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.handler.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.nms.BorderColor
import dev.rosewood.roseskyblock.nms.NMSAdapter
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BorderCommand : SkyblockCommand("border") {

    override fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>) {
        val locale = plugin.getManager(LocaleManager::class.java)

        if (sender !is Player) {
            locale.sendMessage(sender, "misc-player-only")
            return
        }

        if (args.isEmpty()) {
            locale.sendMessage(sender, "command-border-usage")
            return
        }

//        val worldManager = plugin.getManager(WorldManager::class.java)
//
//        val worldGroup = worldManager.worldGroups.firstOrNull()
//
//        if (worldGroup == null) {
//            println("invalid world group, valid types: " + worldManager.worldGroups.map { it.name }.toString())
//            return
//        }
//
//        val group = plugin.getManager(DataManager::class.java).getIslandGroup(sender, worldGroup)
//        // TODO, Send config message
//        if (group == null) {
//            println("No group.")
//            return
//        }
//
//        val island = group.islands.find { it.islandGroup.members.containsKey(sender.uniqueId) }
//
//        if (island == null) {
//            // TODO
//            println("island null")
//            return
//        }

        val border = BorderColor.values().find { it.name.equals(args[0], true) }

        if (border == null) {
            locale.sendMessage(sender, "command-border-invalid-color")
            return
        }


        /// TODO, Send world border for island size and create border at island.

        sender.teleport(sender.location)
        NMSAdapter.handler.sendWorldBorder(sender, border, 50.0, sender.location)
        locale.sendMessage(sender, "command-border-changed", StringPlaceholders.single("color", StringUtils.capitalize(border.name.lowercase())))

    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): List<String> {
        return if (args.size == 1) {
            BorderColor.values().map { it.name.lowercase() }.toList()
        } else {
            listOf()
        }
    }

}
