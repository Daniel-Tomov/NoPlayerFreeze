package me.cageydinosaur.noplayerfreeze;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class Main extends JavaPlugin {
	boolean toggleFreeze = this.getConfig().getBoolean("toggleFreeze");
	Logger logger = Bukkit.getLogger();
	Essentials essentials = null;

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getCommand("noplayerfreeze").setExecutor(new Commands(this));
		this.getCommand("noplayerfreeze").setTabCompleter(new TabCompletion(this));
		this.saveDefaultConfig();
		this.essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	}

	boolean essentials_support() {
		return this.getConfig().getBoolean("essentials");
	}

	void reload() {
		this.reloadConfig();
		this.toggleFreeze = this.getConfig().getBoolean("toggleFreeze");
	}

	boolean debug() {
		return this.getConfig().getBoolean("debug");
	}
}
