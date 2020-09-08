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
            this["test"] = "test2" // TODO
        }}
    }

}
