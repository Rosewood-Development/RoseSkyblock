package dev.rosewood.roseskyblock

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.utils.NMSUtil
import dev.rosewood.roseskyblock.command.handler.Commander
import dev.rosewood.roseskyblock.database.migrations._1_CreateInitialTables
import dev.rosewood.roseskyblock.listener.PlayerListener
import dev.rosewood.roseskyblock.manager.ConfigurationManager
import dev.rosewood.roseskyblock.manager.DataManager
import dev.rosewood.roseskyblock.manager.IslandManager
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.manager.SchematicManager
import dev.rosewood.roseskyblock.manager.WorldManager
import org.bukkit.Bukkit

class RoseSkyblock : RosePlugin(
    -1,
    8788,
    ConfigurationManager::class.java,
    DataManager::class.java,
    LocaleManager::class.java
) {

    override fun enable() {
        instance = this

        if (NMSUtil.getVersionNumber() < 13) {
            this.logger.severe("RoseSkyblock only supports 1.13.2 and above. The plugin has been disabled.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            this.logger.severe("WorldEdit is a dependency of RoseSkyblock. Please install it or an async version to continue using this plugin.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }


        // Register Command Handler

        val cmd = this.getCommand("island")
        if (cmd != null) {
            val commander = Commander(this)

            cmd.setExecutor(commander)
            cmd.tabCompleter = commander
        }

        Bukkit.getPluginManager().registerEvents(PlayerListener(this), this)
    }

    override fun disable() {
    }

    override fun getManagerLoadPriority() = listOf(
        SchematicManager::class.java,
        WorldManager::class.java,
        IslandManager::class.java
    )

    override fun getDataMigrations() = listOf(
        _1_CreateInitialTables::class.java
    )

    companion object {
        @JvmStatic
        lateinit var instance: RoseSkyblock
            private set
    }

}
