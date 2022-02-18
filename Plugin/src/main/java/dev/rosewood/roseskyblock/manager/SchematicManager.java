package dev.rosewood.roseskyblock.manager;

import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.roseskyblock.util.SkyblockUtil;
import dev.rosewood.roseskyblock.world.IslandSchematic;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SchematicManager extends Manager {

    private final Map<String, IslandSchematic> schematics = new HashMap<>();

    public SchematicManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        File schematicsFolder = new File(this.rosePlugin.getDataFolder(), "schematics");
        File schematicsFile = new File(this.rosePlugin.getDataFolder(), "schematics.yml");

        boolean exists = schematicsFile.exists();
        CommentedFileConfiguration schematicsConfig = CommentedFileConfiguration.loadConfiguration(schematicsFile);
        if (!exists) {
            SkyblockUtil.copyResourceTo(this.rosePlugin, "default.schem", new File(schematicsFolder, "default.schem"));
            this.saveDefaults(schematicsConfig);
        }

        File[] schematicFiles = schematicsFolder.listFiles();
        if (schematicFiles == null) {
            throw new NullPointerException("Schematics directory does not exist.");
        }

        schematicsConfig.getKeys(false).forEach(schematicName -> {
            try {
                Optional<File> file = Arrays.stream(schematicFiles)
                        .filter(x -> SkyblockUtil.getNameWithoutExtension(x).equalsIgnoreCase(schematicName))
                        .findFirst();

                if (file.isPresent()) {
                    if (ClipboardFormats.findByFile(file.get()) != null) {
                        CommentedConfigurationSection section = schematicsConfig.getConfigurationSection(schematicName);
                        // added all these throw errors here because thats just kinda how it worked previously
                        if (section == null)
                            throw new IllegalStateException(schematicName);

                        String displayName = section.getString("name");
                        if (displayName == null)
                            throw new IllegalStateException(schematicName + ".name");

                        Material icon = SkyblockUtil.parseEnum(Material.class, section.getString("icon"));
                        List<String> lore = section.getStringList("lore");
                        this.schematics.put(schematicName.toLowerCase(), new IslandSchematic(schematicName, file.get(), displayName, icon, lore));
                    } else {
                        this.rosePlugin.getLogger().warning("File located in the schematics folder is not a valid schematic: " + file.get().getName());
                    }
                } else {
                    this.rosePlugin.getLogger().warning("Unable to locate a schematic that was listed in the schematics.yml file: " + schematicName);
                }
            } catch (Exception ex) {
                this.rosePlugin.getLogger().severe("Missing schematics.yml section: " + ex.getMessage());
            }
        });
    }

    private void saveDefaults(CommentedFileConfiguration config) {
        CommentedConfigurationSection section = config.createSection("default");
        section.set("name", "&eDefault Island");
        section.set("icon", Material.GRASS_BLOCK.name());
        section.set("lore", Arrays.asList(
                "&7The default schematic for RoseSkyblock.",
                "&7Handle with care."
        ));

        config.save();
    }

    @Override
    public void disable() {
        this.schematics.clear();
    }

    public Map<String, IslandSchematic> getSchematics() {
        return schematics;
    }

}
