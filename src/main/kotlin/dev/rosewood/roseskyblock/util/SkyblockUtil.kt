package dev.rosewood.roseskyblock.util

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.world.IslandWorld
import java.io.File
import java.nio.file.Files
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.reflect.KClass
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

/**
 * Gets a manager instance.
 * Convenience method to avoid needing to put .java at the end of a KClass reference.
 *
 * @param managerClass The class of the manager to get
 * @param <T> extends Manager
 * @return A new or existing instance of the given manager class
 */
fun <T : Manager> RosePlugin.getManager(managerClass: KClass<T>): T = this.getManager(managerClass.java)

fun RosePlugin.runAsync(function: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(this, function)

fun RosePlugin.runSync(function: () -> Unit) = Bukkit.getScheduler().runTask(this, function)

fun <T : Enum<T>> parseEnum(enum: KClass<T>, value: String): T {
    try {
        return enum.java.enumConstants.first { it.name.equals(value, true) } ?: error("")
    } catch (ex: Exception) {
        error("Invalid ${enum.simpleName} specified: $value")
    }
}

fun JavaPlugin.copyResourceTo(resourcePath: String, outputDirectory: File) {
    outputDirectory.parentFile.mkdirs()

    val resource = this.getResource(resourcePath)
    requireNotNull(resource) { "Resource does not exist in jar." }

    Files.copy(resource, outputDirectory.toPath())
}

// https://stackoverflow.com/a/19287714
fun getNextIslandLocation(locationId: Int, islandWorld: IslandWorld): Location {
    if (locationId == 0)
        return Location(islandWorld.world, 0.0, islandWorld.islandHeight.toDouble(), 0.0)

    val n = locationId - 1
    val r = floor((sqrt(n + 1.0) - 1) / 2) + 1
    val p = (8 * r * (r - 1)) / 2
    val en = r * 2
    val a = (1 + n - p) % (r * 8)

    var x = 0.0
    var z = 0.0

    when (floor(a / (r * 2)).toInt()) {
        0 -> {
            x = a - r
            z = -r
        }
        1 -> {
            x = r
            z = (a % en) - r
        }
        2 -> {
            x = r - (a % en)
            z = r
        }
        3 -> {
            x = -r
            z = r - (a % en)
        }
    }

    return Location(islandWorld.world, x * 1200, islandWorld.islandHeight.toDouble(), z * 1200)
}
