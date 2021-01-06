package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.CommentedFileConfiguration
import dev.rosewood.rosegarden.config.RoseSetting
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager
import dev.rosewood.roseskyblock.RoseSkyblock
import dev.rosewood.roseskyblock.util.getManager

class ConfigurationManager(rosePlugin: RosePlugin) : AbstractConfigurationManager(rosePlugin, Setting::class.java) {

    enum class Setting(private val key: String, private val defaultValue: Any, private vararg val comments: String) :
        RoseSetting {

        /* TODO: Add some actual enum values here */;

        private var value: Any? = null

        override fun getKey() = this.key

        override fun getDefaultValue() = this.defaultValue

        override fun getComments() = this.comments

        override fun getCachedValue() = this.value

        override fun setCachedValue(value: Any?) {
            this.value = value
        }

        override fun getBaseConfig(): CommentedFileConfiguration? = RoseSkyblock.instance.getManager<ConfigurationManager>().config

    }

    override fun getHeader() = arrayOf(
        "     __________                      ___________           ___     __                 __",
        "     \\______   \\ ____  ______ ____  /   _____/  | _____ __ \\_ |__ |  |   ____   ____ |  | __",
        "      |       _//  _ \\/  ___// __ \\ \\_____  \\|  |/ <   |  | | __ \\|  |  /  _ \\_/ ___\\|  |/ /",
        "      |    |   (  <_> )___ \\\\  ___/ /        \\    < \\___  | | \\_\\ \\  |_(  <_> )  \\___|    <",
        "      |____|_  /\\____/____  >\\___  >_______  /__|_ \\/ ____| |___  /____/\\____/ \\___  >__|_ \\",
        "             \\/           \\/     \\/        \\/     \\/\\/          \\/                 \\/     \\/"
    )

}
