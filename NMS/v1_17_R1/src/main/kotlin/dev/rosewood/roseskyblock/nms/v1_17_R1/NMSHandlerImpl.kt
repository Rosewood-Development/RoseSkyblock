package dev.rosewood.roseskyblock.nms.v1_17_R1

import dev.rosewood.roseskyblock.nms.BorderColor
import dev.rosewood.roseskyblock.nms.NMSHandler
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket
import net.minecraft.world.level.border.WorldBorder
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Player

class NMSHandlerImpl : NMSHandler {

    override fun sendWorldBorder(player: Player, color: BorderColor, size: Double, center: Location) {
        val worldBorder = WorldBorder()
        worldBorder.world = (center.world as CraftWorld).handle
        worldBorder.setCenter(center.blockX + 0.5, center.blockZ + 0.5)

        if (color == BorderColor.OFF) {
            worldBorder.size = Double.MAX_VALUE
        } else {
            worldBorder.size = size
        }

        worldBorder.warningBlocks = 0
        worldBorder.warningTime = 0

        if (color == BorderColor.RED) {
            worldBorder.lerpSizeBetween(size, size - 1.0, 20000000L)
        } else if (color == BorderColor.GREEN) {
            worldBorder.lerpSizeBetween(size - 0.1, size, 20000000L)
        }

        (player as CraftPlayer).handle.connection.send(ClientboundInitializeBorderPacket(worldBorder))
    }

}
