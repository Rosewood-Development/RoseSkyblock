package dev.rosewood.roseskyblock.hook

import dev.rosewood.rosegarden.RosePlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

@Suppress("unused")
class PlaceholderExpansion(private val rosePlugin: RosePlugin) : PlaceholderExpansion() {

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        if (player == null)
            return ""

        return ""
    }

    override fun canRegister() = true

    override fun persist() = true

    override fun getIdentifier() = this.rosePlugin.description.name.toLowerCase()

    override fun getAuthor(): String = this.rosePlugin.description.authors[0]

    override fun getVersion() = this.rosePlugin.description.version

}
