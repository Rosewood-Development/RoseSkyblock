package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.CommentedFileConfiguration
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.util.parseEnum
import dev.rosewood.roseskyblock.world.IslandWorld
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import dev.rosewood.roseskyblock.world.PortalLinks
import dev.rosewood.roseskyblock.world.generator.ChunkLayer
import dev.rosewood.roseskyblock.world.generator.LayeredChunkGenerator
import java.io.File
import kotlin.reflect.KClass
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.block.Biome

class WorldManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val _worldGroups = mutableListOf<IslandWorldGroup>()
    val worldGroups: List<IslandWorldGroup>
        get() = this._worldGroups.toList()

    private var hasReloaded = false

    override fun reload() {
        val worldsFile = File(this.rosePlugin.dataFolder, "worlds.yml")
        val exists = worldsFile.exists()

        val worldsConfig = CommentedFileConfiguration.loadConfiguration(worldsFile)
        if (!exists)
            this.saveDefaults(worldsConfig)

        worldsConfig.getKeys(false).forEach { worldGroupName ->
            try {
                val groupSection = worldsConfig.getConfigurationSection(worldGroupName) ?: error(worldGroupName)
                val displayName = groupSection.getString("name") ?: error("${worldGroupName}.name")
                val startingWorldName = groupSection.getString("starting-world") ?: error("${worldGroupName}.starting-world")
                var startingWorld: IslandWorld? = null
                val gamemode = parseEnum(GameMode::class, (groupSection.getString("gamemode") ?: error("${worldGroupName}.gamemode")).toUpperCase())
                val islandWorlds: MutableList<IslandWorld> = mutableListOf()
                val worldsSection = groupSection.getConfigurationSection("worlds") ?: error("${worldGroupName}.worlds")
                worldsSection.getKeys(false).forEach { worldName ->
                    val worldSection = worldsSection.getConfigurationSection(worldName) ?: error("${worldGroupName}.worlds.$worldName")
                    val worldDisplayName = worldSection.getString("name") ?: error("${worldGroupName}.worlds.${worldName}.name")
                    val worldEnvironment = parseEnum(World.Environment::class, (worldSection.getString("environment") ?: error("${worldGroupName}.worlds.${worldName}.environment")).toUpperCase())
                    val worldBiome = parseEnum(Biome::class, (worldSection.getString("biome") ?: error("${worldGroupName}.worlds.${worldName}.biome")).toUpperCase())
                    val worldGenerationSection = worldSection.getConfigurationSection("world-generation")
                    val worldChunkLayers: List<ChunkLayer> = if (worldGenerationSection == null) {
                        listOf()
                    } else {
                        val chunkLayers: MutableList<ChunkLayer> = mutableListOf()
                        worldGenerationSection.getKeys(false).forEach { layers ->
                            val layerMaterialName = worldGenerationSection.getString(layers) ?: error("${worldGroupName}.worlds.${worldName}.world-generation.$layers")
                            val layerMaterial = Material.matchMaterial(layerMaterialName) ?: error("Invalid Material specified: $layerMaterialName")
                            try {
                                if (layers.contains('-')) {
                                    val split = layers.split('-')
                                    chunkLayers.add(ChunkLayer(Integer.parseInt(split[0]), Integer.parseInt(split[1]), layerMaterial))
                                } else {
                                    chunkLayers.add(ChunkLayer(Integer.parseInt(layers), layerMaterial))
                                }
                            } catch (ex: NumberFormatException) {
                                error("Invalid world-generation range: $layers")
                            }
                        }

                        chunkLayers
                    }
                    val portalLinksSection = worldSection.getConfigurationSection("portal-links")
                    val worldPortalLinks: PortalLinks = if (portalLinksSection == null) {
                        PortalLinks(null, null)
                    } else {
                        PortalLinks(portalLinksSection.getString("nether"), portalLinksSection.getString("end"))
                    }
                    val worldIslandHeight = worldSection.getInt("island-height", -1)
                    if (worldIslandHeight == -1)
                        error("${worldGroupName}.worlds.${worldName}.island-height")

                    val islandWorld = IslandWorld(
                        worldName,
                        worldDisplayName,
                        worldEnvironment,
                        worldBiome,
                        worldChunkLayers,
                        worldPortalLinks,
                        worldIslandHeight
                    )

                    if (worldName == startingWorldName)
                        startingWorld = islandWorld

                    islandWorlds.add(islandWorld)
                }

                if (startingWorld == null)
                    error("${worldGroupName}.starting-world does not match any worlds in this group")

                this._worldGroups.add(IslandWorldGroup(
                    worldGroupName,
                    displayName,
                    startingWorld!!,
                    gamemode,
                    islandWorlds
                ))
            } catch (ex: IllegalStateException) {
                this.rosePlugin.logger.severe("Missing worlds.yml section: ${ex.message}")
            }
        }

        // Create/Load worlds
        this._worldGroups.forEach { worldGroup ->
            worldGroup.worlds.forEach { world ->
                if (Bukkit.getWorld(world.worldName) == null) {
                    Bukkit.createWorld(
                        WorldCreator.name(world.worldName)
                            .generator(LayeredChunkGenerator(world.chunkLayers, world.biome))
                            .environment(world.environment)
                    )
                }
            }
        }

        if (this.hasReloaded) {
            this.rosePlugin.logger.warning("The plugin was reloaded. If any changes were made to the worlds.yml file, you may need to restart your server for them to go into effect.")
        } else {
            this.hasReloaded = true
        }
    }

    override fun disable() {
        this._worldGroups.clear()
    }

    // TODO
    private fun saveDefaults(config: CommentedFileConfiguration) {
        val overworld = IslandWorld(
            "skyblock_survival_overworld",
            "&eSurvival &aOverworld",
            World.Environment.NORMAL,
            Biome.PLAINS,
            listOf(),
            PortalLinks("skyblock_survival_nether", "skyblock_survival_end"),
            64
        )
        val nether = IslandWorld(
            "skyblock_survival_nether",
            "&eSurvival &cNether",
            World.Environment.NETHER,
            Biome.BASALT_DELTAS,
            listOf(ChunkLayer(0, 63, Material.LAVA)),
            PortalLinks("skyblock_survival_overworld", "skyblock_survival_end"),
            64
        )
        val end = IslandWorld(
            "skyblock_survival_end",
            "&eSurvival &7End",
            World.Environment.THE_END,
            Biome.THE_END,
            listOf(),
            PortalLinks("skyblock_survival_nether", "skyblock_survival_overworld"),
            64
        )
        val survivalWorldGroup = IslandWorldGroup(
            "survival",
            "&eSurvival",
            overworld,
            GameMode.SURVIVAL,
            listOf(overworld, nether, end)
        )

        val groupSection = config.createSection(survivalWorldGroup.name)
        groupSection["name"] = survivalWorldGroup.displayName
        groupSection["starting-world"] = survivalWorldGroup.startingWorld.worldName
        groupSection["gamemode"] = survivalWorldGroup.gamemode.name
        val worldsSection = groupSection.createSection("worlds")
        for (world in survivalWorldGroup.worlds) {
            val worldSection = worldsSection.createSection(world.worldName)
            worldSection["name"] = world.displayName
            worldSection["environment"] = world.environment.name
            worldSection["biome"] = world.biome.name
            if (world.chunkLayers.isEmpty()) {
                worldSection["world-generation"] = mutableListOf<String>()
            } else {
                val worldGenSection = worldSection.createSection("world-generation")
                for (chunkLayer in world.chunkLayers) {
                    if (chunkLayer.startLayer == chunkLayer.endLayer) {
                        worldGenSection["${chunkLayer.startLayer}"] = chunkLayer.material.name
                    } else {
                        worldGenSection["${chunkLayer.startLayer}-${chunkLayer.endLayer}"] = chunkLayer.material.name
                    }
                }
            }
            if (world.portalLinks.isEmpty) {
                worldSection["portal-links"] = mutableListOf<String>()
            } else {
                val portalsSection = worldSection.createSection("portal-links")
                world.portalLinks.netherLinkWorldName?.let { portalsSection["nether"] = it }
                world.portalLinks.endLinkWorldName?.let { portalsSection["end"] = it }
            }
            worldSection["island-height"] = world.islandHeight
        }

        config.save()
    }

    fun getIslandWorld(worldName: String): IslandWorld? {
        for (worldGroup in this._worldGroups)
            for (islandWorld in worldGroup.worlds)
                if (islandWorld.worldName == worldName)
                    return islandWorld
        return null
    }

}
