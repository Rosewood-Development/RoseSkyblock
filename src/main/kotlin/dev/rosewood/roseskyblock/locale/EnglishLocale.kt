package dev.rosewood.roseskyblock.locale

import dev.rosewood.rosegarden.locale.Locale
import java.util.LinkedHashMap

class EnglishLocale : Locale {

    override fun getLocaleName(): String {
        return "en_US"
    }

    override fun getTranslatorName(): String {
        return "Esophose"
    }

    override fun getDefaultLocaleValues(): MutableMap<String, Any> {
        return object: LinkedHashMap<String, Any>() { init {
            this["#0"] = "Plugin Message Prefix"
            this["prefix"] = "&7[<g:#8A2387:#E94057:#F27121>RoseSkyblock&7] "

            this["#1"] = "Base Command Message"
            this["base-command-color"] = "&e"
            this["base-command-help"] = "&eUse &b/rsb help &efor command information."

            this["#2"] = "Help Command"
            this["command-help-description"] = "&8 - &d/rsb help &7- Displays the help menu... You have arrived"
            this["command-help-title"] = "&eAvailable Commands:"

            this["#3"] = "Reload Command"
            this["command-reload-description"] = "&8 - &d/rsb reload &7- Reloads the plugin"
            this["command-reload-reloaded"] = "&ePlugin data, configuration, and locale files were reloaded."
        }}
    }

}
