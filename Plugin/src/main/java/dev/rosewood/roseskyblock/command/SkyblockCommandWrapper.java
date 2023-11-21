package dev.rosewood.roseskyblock.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class SkyblockCommandWrapper extends RoseCommandWrapper {

    public SkyblockCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "rsb";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("roseskyblock", "skyblock", "island");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("dev.rosewood.roseskyblock.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
