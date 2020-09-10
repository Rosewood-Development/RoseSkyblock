package dev.rosewood.roseskyblock.hook

import dev.rosewood.rosegarden.RosePlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class RoseSkyblockPlaceholderExpansion(private val rosePlugin: RosePlugin) : PlaceholderExpansion() {

    override fun onPlaceholderRequest(p: Player?, placeholder: String): String? {
        if (p == null)
            return null

        // We got nothing here yet :(
        return null
    }

    override fun persist(): Boolean = true
    override fun getIdentifier(): String = this.rosePlugin.description.name.toLowerCase()
    override fun getAuthor(): String = this.rosePlugin.description.authors[0]
    override fun getVersion(): String = this.rosePlugin.description.version

}
