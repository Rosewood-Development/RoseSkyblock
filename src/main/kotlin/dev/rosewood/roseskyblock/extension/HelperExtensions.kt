package dev.rosewood.roseskyblock.extension

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import kotlin.reflect.KClass

/**
 * Gets a manager instance.
 * Convenience method to avoid needing to put .java at the end of a KClass reference.
 *
 * @param managerClass The class of the manager to get
 * @param <T> extends Manager
 * @return A new or existing instance of the given manager class
 */
fun <T : Manager> RosePlugin.getManager(managerClass: KClass<T>): T = this.getManager(managerClass.java)
