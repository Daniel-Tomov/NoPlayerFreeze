package me.cageydinosaur.noplayerfreeze;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	boolean toggleFreeze = this.getConfig().getBoolean("toggleFreeze");
	Logger logger = Bukkit.getLogger();

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getCommand("noplayerfreeze").setExecutor(new Commands(this));
		this.saveDefaultConfig();
	}

	void reload() {
		this.reloadConfig();
		this.toggleFreeze = this.getConfig().getBoolean("toggleFreeze");
	}
}