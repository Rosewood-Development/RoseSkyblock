package dev.rosewood.roseskyblock.nms

import org.bukkit.Bukkit

object NMSAdapter {

    lateinit var handler: NMSHandler
    var isValidVersion = true
        private set

    init {
        try {
            val name = Bukkit.getServer().javaClass.getPackage().name
            val version = name.substring(name.lastIndexOf('.') + 1)
            this.handler = Class.forName("dev.rosewood.roseskyblock.nms.$version.NMSHandlerImpl").getConstructor().newInstance() as NMSHandler
        } catch (ignored: Exception) {
            this.isValidVersion = false
        }
    }

}
