package dev.lipoteam.treeCapitator;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Configurations {

    FileConfiguration config;

    public Configurations(FileConfiguration config) {
        this.config = config;
    }

    public String prefix() {
        return config.getString("prefix") + " ";
    }

    public ConsoleCommandSender console() {
        return Bukkit.getServer().getConsoleSender();
    }

}
