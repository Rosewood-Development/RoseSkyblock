package dev.rosewood.roseskyblock.world;

public record PortalLinks(String netherLinkWorldName, String endLinkWorld) {

    public boolean isEmpty() {
        return this.netherLinkWorldName == null && this.endLinkWorld == null;
    }

}
