package dev.rosewood.roseskyblock.feature.default

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.utils.StringPlaceholders
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.nms.BorderColor
import dev.rosewood.roseskyblock.nms.NMSAdapter
import java.util.Random
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BorderCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {
        val localeManager = rosePlugin.getManager(LocaleManager::class.java)

        manager.command(builder.literal("border")
            .senderType(Player::class.java)
            .handler { context ->

                // TODO: Add Island Support
                val player = context.sender as Player
                val borderColor = BorderColor.values()[Random().nextInt(BorderColor.values().size)]

                NMSAdapter.handler.sendWorldBorder(player, borderColor, 50.0, player.location)
                localeManager.sendMessage(player, "command-border-changed", StringPlaceholders.single("color", StringUtils.capitalize(borderColor.name.toLowerCase())))
            })
    }

}