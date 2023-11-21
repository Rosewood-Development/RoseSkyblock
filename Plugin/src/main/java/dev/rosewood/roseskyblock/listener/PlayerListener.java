package dev.rosewood.roseskyblock.listener;

import dev.rosewood.rosegarden.RosePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public record PlayerListener(RosePlugin plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

}
