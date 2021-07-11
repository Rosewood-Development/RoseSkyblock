package dev.rosewood.roseskyblock.world

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.session.ClipboardHolder
import dev.rosewood.rosegarden.RosePlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.io.File
import java.io.FileInputStream

class IslandSchematic(val name: String, private val file: File, val displayName: String, val icon: Material, val lore: List<String>) {

    private val clipboardFormat = ClipboardFormats.findByFile(this.file) ?: error("Not a valid schematic: ${this.name}")

    fun paste(rosePlugin: RosePlugin, location: Location, callback: (() -> Unit)? = null) {
        val clipboard: Clipboard

        this.clipboardFormat.getReader(FileInputStream(this.file)).use { clipboard = it.read() }

        val pasteTask = Runnable {
            WorldEdit.getInstance().newEditSessionBuilder()
                .world(BukkitAdapter.adapt(location.world))
                .maxBlocks(-1)
                .build().use {
                    Operations.complete(
                        ClipboardHolder(clipboard)
                            .createPaste(it)
                            .to(BukkitAdapter.asBlockVector(location))
                            .copyEntities(true)
                            .ignoreAirBlocks(true)
                            .build()
                    )
                }

            callback?.invoke()
        }

        if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit") || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit")) {
            Bukkit.getScheduler().runTaskAsynchronously(rosePlugin, pasteTask)
        } else {
            pasteTask.run()
        }

    }

}
