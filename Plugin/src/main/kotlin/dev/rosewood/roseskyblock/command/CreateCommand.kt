package dev.rosewood.roseskyblock.command

import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.handler.SkyblockCommand
import org.bukkit.command.CommandSender

class CreateCommand : SkyblockCommand("create") {

    override fun execute(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>) {
        // TODO
        sender.sendMessage("TODO")
    }

    override fun tabComplete(plugin: RoseSkyblock, sender: CommandSender, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

}