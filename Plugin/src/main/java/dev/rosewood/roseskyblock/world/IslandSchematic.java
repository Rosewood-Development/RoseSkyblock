package dev.rosewood.roseskyblock.world;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.rosewood.roseskyblock.RoseSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public record IslandSchematic(String name, File file, String displayName, Material icon, List<String> lore) {

    /**
     * Paste in an island schematic.
     *
     * @param plugin RoseSkyblock plugin
     * @param location The location of the schematic
     * @param callback Callback function when finished
     */
    public void paste(RoseSkyblock plugin, Location location, Runnable callback) {
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if (format == null) {
            throw new NullPointerException("Not a valid schematic: " + this.name);
        }

        try (FileInputStream inputStream = new FileInputStream(this.file)) {
            clipboard = format.getReader(inputStream).read();
        } catch (IOException ignored) {
            return;
        }

        Clipboard finalClipboard = clipboard;
        Runnable pastTask = () -> {
            EditSession session = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(BukkitAdapter.adapt(location.getWorld()))
                    .maxBlocks(-1)
                    .build();

            try {
                Operations.complete(new ClipboardHolder(finalClipboard).createPaste(session).to(BukkitAdapter.asBlockVector(location)).copyEntities(true).ignoreAirBlocks(true).build());
            } catch (WorldEditException e) {
                e.printStackTrace();
            }

            callback.run();
        };

        if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit") || Bukkit.getPluginManager().isPluginEnabled("AsyncWorldEdit")) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, pastTask);
        } else {
            pastTask.run();
        }
    }

}
