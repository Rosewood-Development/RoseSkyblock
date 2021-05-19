package dev.rosewood.roseskyblock.locale

import dev.rosewood.rosegarden.locale.Locale

class EnglishLocale : Locale {

    override fun getLocaleName() = "en_US"

    override fun getTranslatorName() = "Esophose"

    override fun getDefaultLocaleValues() = object : LinkedHashMap<String, Any>() {
        init {
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

            this["#4"] = "Border Command"
            this["command-border-description"] = "&8 - &d/rsb border <color> &7- Sets the island border color"
            this["command-border-changed"] = "&eSet the island border to %color%."
            this["command-border-usage"] = "&cUsage: &d/island border <color>."
            // May wanna change this if the name is a bit too wack, note for nicole
            this["command-border-invalid-color"] = "&cThe color you specified is not available."

            this["#5"] = "Misc Messages"
            this["misc-player-only"] = "&cOnly players may execute this command."
        }
    }
}
