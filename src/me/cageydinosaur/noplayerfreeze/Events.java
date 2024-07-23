package me.cageydinosaur.noplayerfreeze;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
	Main plugin;

	public Events(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (plugin.toggleFreeze) {
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			if (players.size() == 1) {
				ServerTickManager serverTickManager = Bukkit.getServerTickManager();
				serverTickManager.setFrozen(true);
			}

		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ServerTickManager serverTickManager = Bukkit.getServerTickManager();
		serverTickManager.setFrozen(false);

	}
}
