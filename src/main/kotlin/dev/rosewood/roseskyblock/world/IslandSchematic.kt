package dev.rosewood.roseskyblock.world

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.session.ClipboardHolder
import dev.rosewood.rosegarden.RosePlugin
import java.io.File
import java.io.FileInputStream
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

class IslandSchematic(val name: String, private val file: File, val displayName: String, val icon: Material, val lore: List<String>) {

    private val clipboardFormat = ClipboardFormats.findByFile(this.file) ?: error("Not a valid schematic: ${this.name}")

    fun paste(rosePlugin: RosePlugin, location: Location) {
        val clipboard: Clipboard
        this.clipboardFormat.getReader(FileInputStream(this.file)).use { clipboard = it.read() }

        val pasteTask = Runnable {
//            WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(location.world)).build().use {
//                val operation = ClipboardHolder(clipboard)
//                    .createPaste(it)
//                    .to(BukkitAdapter.asBlockVector(location))
//                    .copyEntities(true)
//                    .ignoreAirBlocks(true)
//                    .build()
//                Operations.complete(operation)
//            }

            // FastAsyncWorldEdit isn't updated to include the non-deprecated version yet
            @Suppress("DEPRECATION")
            WorldEdit.getInstance().editSessionFactory.getEditSession(BukkitAdapter.adapt(location.world), -1).use {
                val operation = ClipboardHolder(clipboard)
                    .createPaste(it)
                    .to(BukkitAdapter.asBlockVector(location))
                    .copyEntities(true)
                    .ignoreAirBlocks(true)
                    .build()
                Operations.complete(operation)
            }
        }

        val pluginManager = Bukkit.getPluginManager()
        if (pluginManager.isPluginEnabled("FastAsyncWorldEdit") || pluginManager.isPluginEnabled("AsyncWorldEdit")) {
            Bukkit.getScheduler().runTaskAsynchronously(rosePlugin, pasteTask)
        } else {
            pasteTask.run()
        }
    }

}
