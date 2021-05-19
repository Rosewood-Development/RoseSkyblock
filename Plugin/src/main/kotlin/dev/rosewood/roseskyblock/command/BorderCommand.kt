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

        if (args.size != 2) {
            locale.sendMessage(sender, "command-border-usage")
            return
        }

        // TODO, Add check for if player has island.

        val border: BorderColor?

        try {
            border = BorderColor.valueOf(args[1].toUpperCase())
        } catch (ex: IllegalArgumentException) {
            locale.sendMessage(sender, "command-border-invalid-color")
            return
        }

        /// TODO, Send world border for island size and create border at island.
        NMSAdapter.handler.sendWorldBorder(sender, border, 50.0, sender.location)
        locale.sendMessage(sender, "command-border-changed", StringPlaceholders.single("color", StringUtils.capitalize(border.name.toLowerCase())))

    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): MutableList<String> {
        return BorderColor.values().map { it.name.toLowerCase() }.toMutableList()
    }

}