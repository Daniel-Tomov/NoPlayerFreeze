package me.cageydinosaur.noplayerfreeze;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	boolean toggleFreeze = true;

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getCommand("noplayerfreeze").setExecutor(new Commands(this));
	}
}
