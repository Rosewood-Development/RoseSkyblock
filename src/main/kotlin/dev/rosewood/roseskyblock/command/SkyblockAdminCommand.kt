package dev.rosewood.roseskyblock.command

import dev.jorel.commandapi.arguments.LiteralArgument
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.roseskyblock.command.SkyblockCommand
import dev.rosewood.roseskyblock.command.SkyblockCommandArgument

abstract class SkyblockAdminCommand(rosePlugin: RosePlugin) : SkyblockCommand(rosePlugin) {

    override val prependedArguments: List<SkyblockCommandArgument> = listOf(
        SkyblockCommandArgument(LiteralArgument("admin"), false)
    )

}
