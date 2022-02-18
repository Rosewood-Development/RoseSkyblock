package dev.rosewood.roseskyblock.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.AbstractCommandManager;

import java.util.Collections;
import java.util.List;

public class CommandManager extends AbstractCommandManager {

    public CommandManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public List<String> getCommandPackages() {
        return Collections.singletonList("dev.rosewood.roseskyblock.command.command");
    }

    @Override
    public List<String> getArgumentHandlerPackages() {
        return Collections.singletonList("dev.rosewood.roseskyblock.command.argument");
    }

    @Override
    public String getCommandName() {
        return "rsb";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("island");
    }
}
