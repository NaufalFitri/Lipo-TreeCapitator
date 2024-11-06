package dev.lipoteam.treeCapitator;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands {

    public Commands(Configurations config) {

        TreeCapitator plugin = TreeCapitator.getInstance();
        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        new CommandAPICommand("treecapitator")
                .withAliases("tc")
                .withPermission("treecapitator.commands")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("treecapitator.commands.reload")
                        .executes((sender, args) -> {
                            if (sender instanceof Player) {
                                sender.sendMessage(config.prefix("Reloaded"));
                            } else {
                                console.sendMessage(config.prefix("Reloaded"));
                            }
                            plugin.ReloadConfig();
                        }))
                .register();
    }



}
