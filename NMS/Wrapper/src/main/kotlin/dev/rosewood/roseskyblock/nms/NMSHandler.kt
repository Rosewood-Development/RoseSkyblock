package dev.rosewood.roseskyblock.nms

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player

interface NMSHandler {

    /**
     * Send a worldborder to a player
     *
     * @param player The CraftPlayer who is getting the border
     * @param color The color of the border
     * @param size The size of the border
     * @param center The center location of the border
     */
    fun sendWorldBorder(player: Player, color: BorderColor, size: Double, center: Location)


}