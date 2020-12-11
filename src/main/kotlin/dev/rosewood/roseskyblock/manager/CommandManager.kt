package dev.rosewood.roseskyblock.manager

import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.bukkit.parsers.WorldArgument
import cloud.commandframework.captions.FactoryDelegatingCaptionRegistry
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.command.SkyblockCaptionKeys
import dev.rosewood.roseskyblock.command.argument.IslandSchematicArgument
import dev.rosewood.roseskyblock.command.argument.IslandWorldGroupArgument
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.util.getNextIslandLocation
import dev.rosewood.roseskyblock.world.IslandSchematic
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import io.leangen.geantyref.TypeToken
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val manager: PaperCommandManager<CommandSender>

    init {
        val executionCoordinatorFunction = CommandExecutionCoordinator.simpleCoordinator<CommandSender>()
        this.manager = PaperCommandManager(rosePlugin, executionCoordinatorFunction, { it }, { it })

        if (this.manager.queryCapability(CloudBukkitCapabilities.BRIGADIER))
            this.manager.registerBrigadier()

        if (this.manager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION))
            this.manager.registerAsynchronousCompletions()

        // Custom arguments
        this.manager.parserRegistry.registerParserSupplier(
            TypeToken.get(
                IslandWorldGroup::class.java
            )
        ) { IslandWorldGroupArgument.IslandWorldGroupParser() }

        this.manager.parserRegistry.registerParserSupplier(
            TypeToken.get(
                IslandSchematic::class.java
            )
        ) { IslandSchematicArgument.IslandSchematicParser() }

        // Custom messages
        val captionRegistry = this.manager.captionRegistry as FactoryDelegatingCaptionRegistry<CommandSender>
        captionRegistry.registerMessageFactory(
            SkyblockCaptionKeys.ARGUMENT_PARSE_FAILURE_ISLAND_WORLD_GROUP
        ) { _, _ -> "'{input}' is not a valid island world group" }
        captionRegistry.registerMessageFactory(
            SkyblockCaptionKeys.ARGUMENT_PARSE_FAILURE_ISLAND_SCHEMATIC
        ) { _, _ -> "'{input}' is not a valid island schematic" }

        val builder = this.manager.commandBuilder("skyblock", "sb", "is", "island", "rsb", "roseskyblock")

        // Base command
        builder.handler { context ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            val baseColor: String = localeManager.getLocaleMessage("base-command-color")
            localeManager.sendCustomMessage(
                context.sender,
                baseColor + "Running <g:#8A2387:#E94057:#F27121>RoseSkyblock" + baseColor + " v" + this.rosePlugin.description.version
            )
            localeManager.sendCustomMessage(
                context.sender,
                baseColor + "Plugin created by: <g:#41e0f0:#ff8dce>" + this.rosePlugin.description.authors[0]
            )
            localeManager.sendSimpleMessage(context.sender, "base-command-help")
        }

        // Help command
        this.manager.command(builder.literal("help").handler { context ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            localeManager.sendMessage(context.sender, "command-help-title")
            localeManager.sendSimpleMessage(context.sender, "command-help-description")
            localeManager.sendSimpleMessage(context.sender, "command-reload-description")
        })

        // Create command
        val worldGroupArgument = IslandWorldGroupArgument.of<CommandSender>("worldGroup")
        val schematicArgument = IslandSchematicArgument.of<CommandSender>("schematic")
        this.manager.command(builder.literal("create", "new")
            .argument(worldGroupArgument)
            .argument(schematicArgument)
            .senderType(Player::class.java)
            .handler { context ->
                val player = context.sender as Player
                val worldGroup = context.get(worldGroupArgument)
                val schematic = context.get(schematicArgument)

                //this.rosePlugin.getManager(DataManager::class).hasIsland(player)

                val pasteLocation = getNextIslandLocation(0, worldGroup.startingWorld)
                schematic.paste(this.rosePlugin, pasteLocation)
                player.teleport(pasteLocation)

                // TODO: IslandManager#createNewIsland
            })

        // Teleport command (Temporary)
        val worldArgument = WorldArgument.of<CommandSender>("world")
        this.manager.command(builder.literal("teleport", "tp", "go", "home")
            .argument(worldArgument)
            .senderType(Player::class.java)
            .handler { context ->
                // TODO: Temporary command
                val player = context.sender as Player
                val world = context.get(worldArgument)
                val location = player.location.clone()
                location.world = world
                player.teleport(location)
            })

        // Admin base command
        val adminBuilder = builder.literal("admin")

        this.manager.command(adminBuilder.literal("reload").handler { context ->
            val localeManager = this.rosePlugin.getManager(LocaleManager::class)
            this.rosePlugin.reload()
            localeManager.sendMessage(context.sender, "command-reload-reloaded")
        })
    }

    override fun reload() {

    }

    override fun disable() {

    }

}
