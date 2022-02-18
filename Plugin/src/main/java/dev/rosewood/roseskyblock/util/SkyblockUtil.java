package dev.rosewood.roseskyblock.util;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.roseskyblock.world.IslandWorld;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

public final class SkyblockUtil {

    public static void copyResourceTo(RosePlugin plugin, String resourcePath, File outputDirectory) {
        outputDirectory.getParentFile().mkdirs();

        InputStream resource = plugin.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("Resource does not exist in jar.");
        }

        try {
            Files.copy(resource, outputDirectory.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // https://stackoverflow.com/a/19287714
    public static Location getNextIslandLocation(int locationId, IslandWorld islandWorld) {
        if (locationId == 0)
            return new Location(islandWorld.getWorld(), 0.0, islandWorld.islandHeight(), 0.0);

        int n = locationId - 1;
        double r = Math.floor((Math.sqrt(n + 1.0) - 1) / 2) + 1;
        double p = (8 * r * (r - 1)) / 2;
        double en = r * 2;
        double a = (1 + n - p) % (r * 8);

        double x = 0.0;
        double z = 0.0;
        switch ((int) Math.floor(a / (r * 2))) {
            case 0 -> {
                x = a - r;
                z = -r;
            }
            case 1 -> {
                x = r;
                z = (a % en) - r;
            }
            case 2 -> {
                x = r - (a % en);
                z = r;
            }
            case 3 -> {
                x = -r;
                z = r - (a % en);
            }
        }

        return new Location(islandWorld.getWorld(), x * 1200, islandWorld.islandHeight(), z * 1200);
    }

    /**
     * Get a file name without the extension
     *
     * @param file The file
     * @return The name of the file
     */
    public static String getNameWithoutExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(0, index);
    }

    public static <T extends Enum<T>> T parseEnum(Class<T> obj, String value) {
        Optional<T> result = Arrays.stream(obj.getEnumConstants()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst();
        if (result.isEmpty())
            throw new NullPointerException("Invalid " + obj.getSimpleName() + " specified: " + value);

        return result.get();
    }

}
