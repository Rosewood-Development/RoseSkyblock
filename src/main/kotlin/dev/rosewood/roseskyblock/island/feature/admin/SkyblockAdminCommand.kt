package dev.rosewood.roseskyblock.island.feature.admin

import dev.jorel.commandapi.arguments.LiteralArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.island.feature.SkyblockCommand
import dev.rosewood.roseskyblock.island.feature.SkyblockCommandArgument

abstract class SkyblockAdminCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val prependedArguments: List<SkyblockCommandArgument> = listOf(
        SkyblockCommandArgument("admin", LiteralArgument("admin"), false)
    )

}
