package dev.rosewood.roseskyblock.locale;

import dev.rosewood.rosegarden.locale.Locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnglishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "en_US";
    }

    @Override
    public String getTranslatorName() {
        return "Esophose";
    }

    @Override
    public Map<String, Object> getDefaultLocaleValues() {
        return new LinkedHashMap<>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#8A2387:#E94057:#F27121>RoseSkyblock&7] ");

            this.put("#1", "Base Command Message");
            this.put("base-command-color", "&e");
            this.put("base-command-help", "&eUse &b/rsb help &efor command information.");

            this.put("#2", "Help Command");
            this.put("command-help-description", "&8 - &d/rsb help &7- Displays the help menu... You have arrived");
            this.put("command-help-title", "&eAvailable Commands:");

            this.put("#3", "Reload Command");
            this.put("command-reload-description", "&8 - &d/rsb reload &7- Reloads the plugin");
            this.put("command-reload-reloaded", "&ePlugin data, configuration, and locale files were reloaded.");

            this.put("#4", "Border Command");
            this.put("command-border-description", "&8 - &d/rsb border <color> &7- Sets the island border color");
            this.put("command-border-changed", "&eSet the island border to %color%.");
            this.put("command-border-usage", "&cUsage: &d/island border <color>.");
            // May wanna change this if the name is a bit too wack, note for nicole
            this.put("command-border-invalid-color", "&cThe color you specified is not available.");

            this.put("#5", "Misc Messages");
            this.put("misc-player-only", "&cOnly players may execute this command.");
        }};
    }
}
