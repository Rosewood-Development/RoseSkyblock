package dev.rosewood.roseskyblock.command.argument

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.captions.CaptionVariable
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import cloud.commandframework.exceptions.parsing.ParserException
import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.command.SkyblockCaptionKeys
import dev.rosewood.roseskyblock.manager.WorldManager
import dev.rosewood.roseskyblock.util.getManager
import dev.rosewood.roseskyblock.world.IslandWorldGroup
import java.util.Queue
import java.util.function.BiFunction

/**
 * RoseSkyblock argument type that parses island world groups
 *
 * @param <C> Command sender type
 */
class IslandWorldGroupArgument<C> private constructor(
    required: Boolean,
    name: String,
    defaultValue: String,
    suggestionsProvider: BiFunction<CommandContext<C>, String, List<String>>?
) : CommandArgument<C, IslandWorldGroup>(
    required,
    name,
    IslandWorldGroupParser(),
    defaultValue,
    IslandWorldGroup::class.java,
    suggestionsProvider
) {

    companion object {

        /**
         * Create a new builder
         *
         * @param name Name of the argument
         * @param <C>  Command sender type
         * @return Created builder
         */
        @JvmStatic
        fun <C> newBuilder(name: String): CommandArgument.Builder<C, IslandWorldGroup> {
            return Builder(name)
        }

        /**
         * Create a new required argument
         *
         * @param name Argument name
         * @param <C>  Command sender type
         * @return Created argument
         */
        @JvmStatic
        fun <C> of(name: String): CommandArgument<C, IslandWorldGroup> {
            return this.newBuilder<C>(name).asRequired().build()
        }

        /**
         * Create a new optional argument
         *
         * @param name Argument name
         * @param <C>  Command sender type
         * @return Created argument
        </C> */
        fun <C> optional(name: String): CommandArgument<C, IslandWorldGroup> {
            return this.newBuilder<C>(name).asOptional().build()
        }

        /**
         * Create a new optional argument with a default value
         *
         * @param name         Argument name
         * @param defaultValue Default value
         * @param <C>          Command sender type
         * @return Created argument
         */
        @JvmStatic
        fun <C> optional(name: String, defaultValue: String): CommandArgument<C, IslandWorldGroup> {
            return this.newBuilder<C>(name).asOptionalWithDefault(defaultValue).build()
        }

    }

    class Builder<C>(name: String) : CommandArgument.Builder<C, IslandWorldGroup>(
        IslandWorldGroup::class.java,
        name
    ) {

        override fun build(): CommandArgument<C, IslandWorldGroup> {
            return IslandWorldGroupArgument<C>(this.isRequired, this.name, this.defaultValue, this.suggestionsProvider)
        }

    }

    class IslandWorldGroupParser<C> : ArgumentParser<C, IslandWorldGroup> {

        override fun parse(
            commandContext: CommandContext<C>,
            inputQueue: Queue<String>
        ): ArgumentParseResult<IslandWorldGroup> {
            val input = inputQueue.peek()
                ?: return ArgumentParseResult.failure(
                    NoInputProvidedException(
                        IslandWorldGroupParser::class.java,
                        commandContext
                    )
                )

            val worldManager = RoseSkyblock.instance.getManager(WorldManager::class)
            val islandWorldGroup = worldManager.getIslandWorldGroup(input)
                ?: return ArgumentParseResult.failure(IslandWorldGroupParseException(input, commandContext))

            inputQueue.remove()
            return ArgumentParseResult.success(islandWorldGroup)
        }

        override fun suggestions(commandContext: CommandContext<C>, input: String): List<String> {
            return RoseSkyblock.instance.getManager(WorldManager::class).worldGroups.map { it.name }
        }

    }

    class IslandWorldGroupParseException(val input: String, context: CommandContext<*>) : ParserException(
        IslandWorldGroupParser::class.java,
        context,
        SkyblockCaptionKeys.ARGUMENT_PARSE_FAILURE_ISLAND_WORLD_GROUP,
        CaptionVariable.of("input", input)
    )

}
