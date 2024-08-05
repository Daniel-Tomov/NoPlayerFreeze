package me.cageydinosaur.noplayerfreeze;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
	Main plugin;

	public Commands(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("noplayerfreeze")) {

			if (!sender.hasPermission("noplayerfreeze")) {
				sender.sendMessage(ChatColor.RED + "You cannot use this plugin!");
				return true;
			}

			if (args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "Usage:");
				sender.sendMessage(ChatColor.GREEN + "/noplayerfreeze toggle - toggles the plugin");
				sender.sendMessage(ChatColor.GREEN + "/noplayerfreeze reload - reloads the config");
			}
			
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.reload();
				return true;
			}
			if (args[0].equalsIgnoreCase("toggle")) {
				plugin.toggleFreeze = !plugin.toggleFreeze;
				sender.sendMessage(ChatColor.GREEN + "The status of NoPlayerFreeze is " + plugin.toggleFreeze);
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Unknown command");
		}
		return true;
	}
}