package dev.rosewood.roseskyblock.nms.v1_20_R1;

import dev.rosewood.roseskyblock.nms.BorderColor;
import dev.rosewood.roseskyblock.nms.NMSHandler;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandlerImpl implements NMSHandler {

    @Override
    public void sendWorldBorder(Player player, BorderColor color, double size, Location center) {
        WorldBorder border = new WorldBorder();
        border.world = ((CraftWorld) center.getWorld()).getHandle();
        border.setCenter(center.getBlockX() + 0.5, center.getBlockZ() + 0.5);
        border.setWarningBlocks(0);
        border.setWarningTime(0);

        switch (color) {
            case OFF -> border.setSize(Integer.MAX_VALUE);
            case BLUE -> border.setSize(size);
            case RED -> border.lerpSizeBetween(size, size - 1.0, 20000000L);
            case GREEN -> border.lerpSizeBetween(size - 1.0, size, 20000000L);
        }

        ((CraftPlayer) player).getHandle().connection.send(new ClientboundInitializeBorderPacket(border));
    }

}
