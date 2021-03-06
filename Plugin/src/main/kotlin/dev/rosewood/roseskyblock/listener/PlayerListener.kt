package dev.rosewood.roseskyblock.listener

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.manager.IslandManager
import dev.rosewood.roseskyblock.util.getManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused") // the EventHandler methods are called reflectively by Bukkit internally
class PlayerListener(private val rosePlugin: RosePlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onJoin(event: PlayerJoinEvent) {
        val islandManager = this.rosePlugin.getManager<IslandManager>()
        islandManager.tryLoadIslands(event.player)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onQuit(event: PlayerQuitEvent) {
        val islandManager = this.rosePlugin.getManager<IslandManager>()
        islandManager.tryUnloadIslands(event.player)
    }
}
