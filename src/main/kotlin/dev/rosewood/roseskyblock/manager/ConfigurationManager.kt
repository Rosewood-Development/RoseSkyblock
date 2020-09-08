package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.CommentedFileConfiguration
import dev.rosewood.rosegarden.config.RoseSetting
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager
import dev.rosewood.roseskyblock.RoseSkyblock


class ConfigurationManager(rosePlugin: RosePlugin) : AbstractConfigurationManager(rosePlugin, Setting::class.java) {

    enum class Setting(private val key: String?, private val defaultValue: Any?, private vararg val comments: String) : RoseSetting {

        ;

        private var value: Any? = null

        override fun getKey(): String? {
            return key
        }

        override fun getDefaultValue(): Any? {
            return defaultValue
        }

        override fun getComments(): Array<out String> {
            return comments
        }

        override fun getCachedValue(): Any? {
            return value
        }

        override fun setCachedValue(value: Any?) {
            this.value = value
        }

        override fun getBaseConfig(): CommentedFileConfiguration? {
            return RoseSkyblock.instance.getManager(ConfigurationManager::class).config
        }

    }

    override fun getHeader(): Array<String> {
        return arrayOf(
            "     __________                      ___________           ___     __                 __",
            "     \\______   \\ ____  ______ ____  /   _____/  | _____ __ \\_ |__ |  |   ____   ____ |  | __",
            "      |       _//  _ \\/  ___// __ \\ \\_____  \\|  |/ <   |  | | __ \\|  |  /  _ \\_/ ___\\|  |/ /",
            "      |    |   (  <_> )___ \\\\  ___/ /        \\    < \\___  | | \\_\\ \\  |_(  <_> )  \\___|    <",
            "      |____|_  /\\____/____  >\\___  >_______  /__|_ \\/ ____| |___  /____/\\____/ \\___  >__|_ \\",
            "             \\/           \\/     \\/        \\/     \\/\\/          \\/                 \\/     \\/"
        )
    }

}
