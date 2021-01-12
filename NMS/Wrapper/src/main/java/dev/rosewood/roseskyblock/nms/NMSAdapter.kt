package dev.rosewood.roseskyblock.nms

import org.bukkit.Bukkit

// If someone else wants to get this to be better, please do.
object NMSAdapter {
    var handler: NMSHandler? = null
    var isValidVersion = false

    init {
        try {
            val name = Bukkit.getServer().javaClass.getPackage().name
            val version = name.substring(name.lastIndexOf('.') + 1)
            handler = Class.forName("dev.rosewood.roseskyblock.nms.$version.NMSHandlerImpl").getConstructor().newInstance() as NMSHandler
        } catch (ignored: Exception) {
        }
    }
}