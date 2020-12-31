package dev.rosewood.roseskyblock.hook

import dev.rosewood.rosegarden.RosePlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderExpansion(private val rosePlugin: RosePlugin) : PlaceholderExpansion() {

    override fun onPlaceholderRequest(p: Player?, placeholder: String): String? {
        if (p == null)
            return null

        // We got nothing here yet :(
        // sad :(
        return null
    }

    override fun persist() = true
    override fun getIdentifier() = rosePlugin.description.name.toLowerCase()
    override fun getAuthor(): String = rosePlugin.description.authors[0]
    override fun getVersion() = rosePlugin.description.version
}
