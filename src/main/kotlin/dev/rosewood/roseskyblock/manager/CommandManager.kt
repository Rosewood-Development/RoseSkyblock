package dev.rosewood.roseskyblock.manager

import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.captions.FactoryDelegatingCaptionRegistry
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import dev.rosewood.roseskyblock.command.SkyblockCaptionKeys
import dev.rosewood.roseskyblock.command.argument.IslandSchematicArgument
import dev.rosewood.roseskyblock.command.argument.IslandWorldGroupArgument
import dev.rosewood.roseskyblock.feature.admin.reload.ReloadAdminCommand
import dev.rosewood.roseskyblock.feature.create.CreateCommand
import dev.rosewood.roseskyblock.feature.default.DefaultCommand
import dev.rosewood.roseskyblock.feature.help.HelpCommand
import dev.rosewood.roseskyblock.feature.teleport.TeleportCommand
import dev.rosewood.roseskyblock.world.IslandSchematic
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import io.leangen.geantyref.TypeToken
import org.bukkit.command.CommandSender

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

        listOf(
            DefaultCommand(rosePlugin),
            HelpCommand(rosePlugin),
            CreateCommand(rosePlugin),
            TeleportCommand(rosePlugin)
        ).forEach { it.create(this.manager, builder)}

        val adminBuilder = builder.literal("admin")

        listOf(
            ReloadAdminCommand(rosePlugin)
        ).forEach { it.create(this.manager, adminBuilder)}
    }

    override fun reload() {}

    override fun disable() {}

}
