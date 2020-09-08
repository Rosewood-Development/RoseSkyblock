package dev.rosewood.roseskyblock

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.manager.ConfigurationManager
import dev.rosewood.roseskyblock.manager.DataManager
import dev.rosewood.roseskyblock.manager.IslandManager
import dev.rosewood.roseskyblock.manager.LocaleManager
import dev.rosewood.roseskyblock.manager.SchematicManager
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.world.generator.VoidGenerator
import kotlin.reflect.KClass
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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

    // TODO: Don't put this here
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name.equals("skyblock", true) && sender is Player) {
            val pasteLocation = this.getManager(IslandManager::class).getNextIslandLocation(sender.world)
            this.getManager(SchematicManager::class).schematics.values.first().paste(this, sender.location.clone().add(pasteLocation))
            sender.sendMessage(ChatColor.of("#c763a9").toString() + "Pasting schematic!")
        }

        return true
    }

    override fun enable() {
        instance = this

        if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            this.logger.severe("WorldEdit is a dependency of RoseSkyblock. Please install it or an async version to continue using this plugin.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        if (Bukkit.getWorld("skyblock_world") == null) {
            Bukkit.createWorld(WorldCreator.name("skyblock_world")
                .generator(VoidGenerator())
                .generateStructures(false)
                .environment(World.Environment.NORMAL)
            )
        }
    }

    override fun disable() {

    }

    override fun getManagerLoadPriority(): List<Class<out Manager>> {
        return listOf(
            SchematicManager::class.java,
            WorldManager::class.java,
            IslandManager::class.java
        )
    }

    override fun <T : DataMigration> getDataMigrations(): List<Class<T>> {
        return listOf()
    }

    /**
     * Gets a manager instance.
     * Convenience method to avoid needing to put .java at the end of a KClass reference.
     *
     * @param managerClass The class of the manager to get
     * @param <T> extends Manager
     * @return A new or existing instance of the given manager class
     */
    fun <T : Manager> getManager(managerClass: KClass<T>): T {
        return this.getManager(managerClass.java)
    }

}
