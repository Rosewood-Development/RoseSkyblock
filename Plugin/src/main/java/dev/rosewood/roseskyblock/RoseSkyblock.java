package dev.rosewood.roseskyblock;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.roseskyblock.listener.PlayerListener;
import dev.rosewood.roseskyblock.manager.CommandManager;
import dev.rosewood.roseskyblock.manager.ConfigurationManager;
import dev.rosewood.roseskyblock.manager.DataManager;
import dev.rosewood.roseskyblock.manager.IslandManager;
import dev.rosewood.roseskyblock.manager.LocaleManager;
import dev.rosewood.roseskyblock.manager.SchematicManager;
import dev.rosewood.roseskyblock.manager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.List;

public class RoseSkyblock extends RosePlugin {

    private static RoseSkyblock instance;

    public RoseSkyblock() {
        super(-1, 8788, ConfigurationManager.class, DataManager.class, LocaleManager.class, CommandManager.class);

        instance = this;
    }

    @Override
    public void enable() {
        // TODO: Use api-version in plugin.yml
        if (NMSUtil.getVersionNumber() < 17) {
            this.getLogger().severe("RoseSkyblock only supports 1.17 and above. The plugin has been disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            this.getLogger().severe("WorldEdit is a dependency of RoseSkyblock. Please install it or an async version to continue using this plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return Arrays.asList(
                SchematicManager.class,
                WorldManager.class,
                IslandManager.class
        );
    }

    public static RoseSkyblock getInstance() {
        return instance;
    }
}
