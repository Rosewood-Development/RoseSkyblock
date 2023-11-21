package dev.rosewood.roseskyblock.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import dev.rosewood.roseskyblock.RoseSkyblock;

public class ConfigurationManager extends AbstractConfigurationManager {

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    public enum Setting implements RoseSetting {
        ; // TODO: Add some actual enum values here.

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String[] getComments() {
            return comments;
        }

        @Override
        public Object getCachedValue() {
            return value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return RoseSkyblock.getInstance().getManager(ConfigurationManager.class).getConfig();
        }

    }

    @Override
    protected String[] getHeader() {
        return new String[]{"     __________                      ___________           ___     __                 __",
                "     \\______   \\ ____  ______ ____  /   _____/  | _____ __ \\_ |__ |  |   ____   ____ |  | __",
                "      |       _//  _ \\/  ___// __ \\ \\_____  \\|  |/ <   |  | | __ \\|  |  /  _ \\_/ ___\\|  |/ /",
                "      |    |   (  <_> )___ \\\\  ___/ /        \\    < \\___  | | \\_\\ \\  |_(  <_> )  \\___|    <",
                "      |____|_  /\\____/____  >\\___  >_______  /__|_ \\/ ____| |___  /____/\\____/ \\___  >__|_ \\",
                "             \\/           \\/     \\/        \\/     \\/\\/          \\/                 \\/     \\/"
        };
    }
}
