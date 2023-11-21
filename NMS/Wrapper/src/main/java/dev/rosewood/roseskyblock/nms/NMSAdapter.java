package dev.rosewood.roseskyblock.nms;

import org.bukkit.Bukkit;

public class NMSAdapter {

    private NMSHandler handler = null;
    private boolean isValidVersion = true;

    public NMSAdapter() {
        try {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            String version = name.substring(name.lastIndexOf('.') + 1);
            this.handler = (NMSHandler) Class.forName("dev.rosewood.roseskyblock.nms." + version + ".NMSHandlerImpl").getConstructor().newInstance();
        } catch (Exception ignored) {
            this.isValidVersion = false;
        }
    }

    public boolean isValidVersion() {
        return isValidVersion;
    }

    public NMSHandler getHandler() {
        return handler;
    }
}
