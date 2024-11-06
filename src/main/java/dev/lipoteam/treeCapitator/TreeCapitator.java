package dev.lipoteam.treeCapitator;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreeCapitator extends JavaPlugin {

    private final ConsoleCommandSender console = getServer().getConsoleSender();
    private Configurations config;

    private Event event;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).silentLogs(true));
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Configurations(getConfig());
        console.sendMessage(config.prefix("Enabled"));

        RegisterEvents();
        RegisterCommands();

    }

    @Override
    public void onDisable() {
        console.sendMessage(config.prefix("Disabled"));
    }

    private void RegisterEvents() {

        CustomBlockData.registerListener(this);

        Configurations config = new Configurations(getConfig());
        event = new Event(config, this);
        getServer().getPluginManager().registerEvents(event, this);
    }

    private void RegisterCommands() {
        new Commands(new Configurations(getConfig()));
    }

    public static TreeCapitator getInstance() {

        return getPlugin(TreeCapitator.class);

    }

    public void ReloadConfig() {

        this.reloadConfig();
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        FileConfiguration config = this.getConfig();
        Configurations newConfig = new Configurations(config);
        event.setConfig(newConfig);

    }
}
