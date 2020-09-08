package dev.rosewood.roseskyblock

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.database.DataMigration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.manager.ConfigurationManager
import dev.rosewood.roseskyblock.manager.DataManager
import dev.rosewood.roseskyblock.manager.LocaleManager
import kotlin.reflect.KClass

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
    }

    override fun disable() {

    }

    override fun getManagerLoadPriority(): List<Class<out Manager>> {
        return listOf()
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
        return getManager(managerClass.java)
    }

}
