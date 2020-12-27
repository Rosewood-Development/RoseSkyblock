package dev.rosewood.roseskyblock

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.rosegarden.utils.NMSUtil
import dev.rosewood.roseskyblock.database.migrations._1_Create_Table_Island
import dev.rosewood.roseskyblock.database.migrations._2_Create_Table_Island_Member
import dev.rosewood.roseskyblock.database.migrations._3_Create_Table_Island_Group
import dev.rosewood.roseskyblock.listener.PlayerListener
import dev.rosewood.roseskyblock.manager.CommandManager
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

    companion object {
        @JvmStatic
        lateinit var instance: RoseSkyblock
            private set
    }

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

        Bukkit.getPluginManager().registerEvents(PlayerListener(this), this)
    }

    override fun disable() {

    }

    override fun getManagerLoadPriority(): List<Class<out Manager>> {
        return listOf(
            CommandManager::class.java,
            SchematicManager::class.java,
            WorldManager::class.java,
            IslandManager::class.java
        )
    }

    override fun getDataMigrations(): List<Class<out DataMigration>> {
        return listOf(
            _1_Create_Table_Island::class.java,
            _2_Create_Table_Island_Member::class.java,
            _3_Create_Table_Island_Group::class.java,
        )
    }

}
