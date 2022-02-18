package dev.rosewood.roseskyblock.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMSHandler {

    /**
     * Send a worldborder to a Player
     *
     * @param player The layer who is getting the border
     * @param color  The color of the border
     * @param size   The size of the border
     * @param center The center location of the border
     */
    void sendWorldBorder(Player player, BorderColor color, double size, Location center);

}
