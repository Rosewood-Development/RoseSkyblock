package dev.rosewood.roseskyblock.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.locale.Locale
import dev.rosewood.rosegarden.manager.AbstractLocaleManager
import dev.rosewood.roseskyblock.locale.EnglishLocale

class LocaleManager(rosePlugin: RosePlugin) : AbstractLocaleManager(rosePlugin) {

    override fun getLocales() = listOf(EnglishLocale())

}
