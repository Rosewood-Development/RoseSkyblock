package dev.rosewood.roseskyblock.world

// TODO
data class PortalLinks(val netherLinkWorldName: String?, val endLinkWorldName: String?) {

    val isEmpty: Boolean
        get() = this.netherLinkWorldName == null && this.endLinkWorldName == null

}
