package dev.rosewood.roseskyblock.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.roseskyblock.util.SkyblockUtil;
import dev.rosewood.roseskyblock.world.IslandWorld;
import dev.rosewood.roseskyblock.world.IslandWorldGroup;
import dev.rosewood.roseskyblock.world.PortalLinks;
import dev.rosewood.roseskyblock.world.generator.ChunkLayer;
import dev.rosewood.roseskyblock.world.generator.LayeredChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WorldManager extends Manager {

    private final List<IslandWorldGroup> worldGroups = new ArrayList<>();
    private boolean hasReloaded = false;

    public WorldManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        File worldsFile = new File(this.rosePlugin.getDataFolder(), "worlds.yml");
        boolean exists = worldsFile.exists();

        CommentedFileConfiguration worldsConfig = CommentedFileConfiguration.loadConfiguration(worldsFile);
        if (!exists) {
            this.saveDefault(worldsConfig);
        }

        worldsConfig.getKeys(false).forEach(worldGroupName -> {
            try {
                CommentedConfigurationSection groupSection = worldsConfig.getConfigurationSection(worldGroupName);
                if (groupSection == null)
                    throw new NullPointerException(worldGroupName);

                // i really didnt wanna overload this file with throw NullPointerException
                String displayName = groupSection.getString("name");
                String startingWorldName = groupSection.getString("starting-world");
                AtomicReference<IslandWorld> startingWorld = new AtomicReference<>();
                GameMode gamemode = SkyblockUtil.parseEnum(GameMode.class, groupSection.getString("gamemode").toUpperCase());
                List<IslandWorld> islandWorlds = new ArrayList<>();
                CommentedConfigurationSection worldsSection = groupSection.getConfigurationSection("worlds");
                worldsSection.getKeys(false).forEach(worldName -> {
                    CommentedConfigurationSection worldSection = worldsSection.createSection(worldName);
                    String worldDisplayName = worldSection.getString("name");
                    World.Environment worldEnviroment = SkyblockUtil.parseEnum(World.Environment.class, worldSection.getString("enviroment").toUpperCase());
                    Biome worldBiome = SkyblockUtil.parseEnum(Biome.class, worldSection.getString("biome").toUpperCase());

                    CommentedConfigurationSection worldGenerationSection = worldSection.getConfigurationSection("world-generation");
                    List<ChunkLayer> worldChunkLayers;
                    if (worldGenerationSection == null) {
                        worldChunkLayers = new ArrayList<>();
                    } else {
                        List<ChunkLayer> chunkLayers = new ArrayList<>();
                        worldGenerationSection.getKeys(false).forEach(layers -> {
                            Material layerMaterial = SkyblockUtil.parseEnum(Material.class, worldGenerationSection.getString(layers));
                            try {
                                if (layers.contains("-")) {
                                    String[] split = layers.split("-");
                                    chunkLayers.add(new ChunkLayer(Integer.parseInt(split[0]), Integer.parseInt(split[1]), layerMaterial));
                                } else {
                                    chunkLayers.add(new ChunkLayer(Integer.parseInt(layers), Integer.parseInt(layers), layerMaterial));
                                }
                            } catch (NumberFormatException ex) {
                                throw new IllegalStateException("Invalid world-generation range: " + layers);
                            }
                        });

                        worldChunkLayers = chunkLayers;
                    }

                    CommentedConfigurationSection portalLinksSection = worldSection.getConfigurationSection("portal-links");
                    PortalLinks worldPortalLinks = portalLinksSection == null
                            ? new PortalLinks(null, null)
                            : new PortalLinks(portalLinksSection.getString("nether"), portalLinksSection.getString("end"));

                    int worldIslandHeight = worldSection.getInt("island-height", -1);
                    if (worldIslandHeight == -1)
                        throw new IllegalStateException(worldGroupName + ".worlds." + worldName + ".island-height");

                    IslandWorld world = new IslandWorld(
                            worldName,
                            worldDisplayName,
                            worldEnviroment,
                            worldBiome,
                            worldChunkLayers,
                            worldPortalLinks,
                            worldIslandHeight
                    );

                    if (worldName.equalsIgnoreCase(startingWorldName))
                        startingWorld.set(world);

                    islandWorlds.add(world);
                });

                this.worldGroups.add(new IslandWorldGroup(worldGroupName, displayName, startingWorld.get(), gamemode, islandWorlds));
            } catch (Exception ex) {
                this.rosePlugin.getLogger().severe("Missing worlds.yml section: " + ex.getMessage());
            }
        });

        this.worldGroups.forEach(worldGroup -> {
            worldGroup.worlds().forEach(islandWorld -> {
                if (Bukkit.getWorld(islandWorld.worldName()) == null) {
                    Bukkit.createWorld(
                            WorldCreator.name(islandWorld.worldName())
                                    .generator(new LayeredChunkGenerator(islandWorld.layers(), islandWorld.biome()))
                                    .environment(islandWorld.enviroment()));
                }
            });
        });

        if (this.hasReloaded) {
            this.rosePlugin.getLogger().warning("The plugin was reloaded. If any changes were made to the worlds.yml file, you may need to restart your server for them to go into effect.");
        } else {
            this.hasReloaded = true;
        }
    }

    private void saveDefault(CommentedFileConfiguration config) {
        IslandWorld overworld = new IslandWorld(
                "skyblock_survival_overworld",
                "&eSurvival &aOverworld",
                World.Environment.NORMAL,
                Biome.PLAINS,
                new ArrayList<>(),
                new PortalLinks("skyblock_survival_nether", "skyblock_survival_end"),
                64
        );

        IslandWorld nether = new IslandWorld(
                "skyblock_survival_nether",
                "&eSurvival &cNether",
                World.Environment.NETHER,
                Biome.NETHER_WASTES,
                Collections.singletonList(new ChunkLayer(0, 63, Material.LAVA)),
                new PortalLinks("skyblock_survival_overworld", "skyblock_survival_end"),
                64
        );

        IslandWorld end = new IslandWorld(
                "skyblock_survival_end",
                "&eSurvival &7End",
                World.Environment.THE_END,
                Biome.THE_END,
                new ArrayList<>(),
                new PortalLinks("skyblock_survival_nether", "skyblock_survival_overworld"),
                64
        );

        IslandWorldGroup survivalWorldGroup = new IslandWorldGroup(
                "survival",
                "&eSurvival",
                overworld,
                GameMode.SURVIVAL,
                Arrays.asList(overworld, nether, end)
        );

        CommentedConfigurationSection groupSection = config.createSection(survivalWorldGroup.name());
        groupSection.set("name", survivalWorldGroup.displayName());
        groupSection.set("starting-world", survivalWorldGroup.startingWorld().worldName());
        groupSection.set("gamemode", survivalWorldGroup.gamemode().name());
        CommentedConfigurationSection worldsSection = groupSection.createSection("worlds");
        for (IslandWorld world : survivalWorldGroup.worlds()) {
            CommentedConfigurationSection worldSection = worldsSection.createSection(world.worldName());
            worldSection.set("name", world.displayName());
            worldSection.set("enviroment", world.enviroment().name());
            worldSection.set("biome", world.biome().name());
            if (world.layers().isEmpty()) {
                worldSection.set("world-generation", new ArrayList<>());
            } else {
                CommentedConfigurationSection worldGenSection = worldSection.createSection("world-generation");
                for (ChunkLayer layer : world.layers()) {
                    if (layer.startLayer() == layer.endLayer())
                        worldGenSection.set(String.valueOf(layer.startLayer()), layer.material().name());
                    else
                        worldGenSection.set(layer.startLayer() + "-" + layer.endLayer(), layer.material().name());
                }
            }

            if (world.links().isEmpty()) {
                worldsSection.set("portal-links", new ArrayList<>());
            } else {
                CommentedConfigurationSection portalsSection = worldSection.createSection("portal-links");
                if (world.links().netherLinkWorldName() != null)
                    portalsSection.set("nether", world.links().netherLinkWorldName());

                if (world.links().endLinkWorld() != null)
                    portalsSection.set("end", world.links().endLinkWorld());

            }

            worldSection.set("island-height", world.islandHeight());
        }
    }

    public IslandWorld getIslandWorld(String worldName) {
        for (IslandWorldGroup worldGroup : this.worldGroups)
            for (IslandWorld islandWorld : worldGroup.worlds())
                if (islandWorld.worldName().equalsIgnoreCase(worldName))
                    return islandWorld;

        return null;
    }

    public IslandWorldGroup getIslandWorldGroup(String groupName) {
        for (IslandWorldGroup worldGroup : this.worldGroups)
            if (worldGroup.name().equalsIgnoreCase(groupName))
                return worldGroup;

        return null;
    }

    @Override
    public void disable() {

    }

    public List<IslandWorldGroup> getWorldGroups() {
        return worldGroups;
    }

}
