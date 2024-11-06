package dev.lipoteam.treeCapitator;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreeCapitator extends JavaPlugin {

    private ConsoleCommandSender console;
    private Configurations config;

    @Override
    public void onEnable() {

        config = new Configurations(getConfig());
        console = config.console();
        console.sendMessage(config.prefix() + "Enabled");

    }

    @Override
    public void onDisable() {
        console.sendMessage(config.prefix() + "Disabled");
    }
}
