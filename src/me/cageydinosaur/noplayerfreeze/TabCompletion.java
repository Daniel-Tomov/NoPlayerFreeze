package me.cageydinosaur.noplayerfreeze;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {
	Main plugin;

	public TabCompletion(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> commands = new ArrayList<>();
		if (sender.hasPermission("noplayerfreeze")) {
			commands.add("reload");
			commands.add("toggle");
		}
		return commands;
	}
}