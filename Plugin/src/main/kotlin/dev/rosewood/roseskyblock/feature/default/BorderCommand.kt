package dev.rosewood.roseskyblock.feature.default

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.utils.HexUtils.colorify
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.nms.BorderColor
import dev.rosewood.roseskyblock.nms.NMSAdapter
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class BorderCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override fun create(manager: CommandManager<CommandSender>, builder: Command.Builder<CommandSender>) {


        manager.command(builder.literal("border")
            .senderType(Player::class.java)
            .handler { context ->
                val player = context.sender as Player
                val borderColor = BorderColor.values()[Random().nextInt(BorderColor.values().size)]
                NMSAdapter.handler?.sendWorldBorder(player, borderColor, 50.0, player.location)
                player.sendMessage(colorify("<r:0.7:loop>Border Color is now set to ${StringUtils.capitalize(borderColor.name.toLowerCase())}"))
            })
    }

}