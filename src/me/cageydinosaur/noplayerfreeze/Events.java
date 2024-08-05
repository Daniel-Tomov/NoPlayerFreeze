package me.cageydinosaur.noplayerfreeze;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class Events implements Listener {
	Main plugin;
	ServerTickManager serverTickManager;

	public Events(Main plugin) {
		this.plugin = plugin;
		this.serverTickManager = Bukkit.getServerTickManager();
	}

	// returns true if all players have ignore permission or are AFK
	private boolean shouldItBeFrozen(UUID uuid) {
		if (!this.plugin.toggleFreeze) {
			return false;
		}

		int counter = 0;
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		for (Player player : players) {
			if (player.getUniqueId() == uuid) {
				// here when onPlayerLeave event is fired
			} else if (player.hasPermission("noplayerfreeze.ignore")) {
				// hello
			} else {
				counter++;
			}

		}
		return counter == 0;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (!this.plugin.toggleFreeze) {
			return;
		}
		if (this.shouldItBeFrozen(event.getPlayer().getUniqueId())) {
			this.serverTickManager.setFrozen(true);
			this.plugin.logger
					.info("All players in the server have the noplayerfreeze.ignore permission. The server is frozen.");
		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!this.plugin.toggleFreeze) {
			return;
		}
		if (!this.shouldItBeFrozen(new UUID(0, 0))) {
			this.serverTickManager.setFrozen(false);
			this.plugin.logger
					.info("A player without the noplayerfreeze.ignore permission joined. The server is not frozen.");
		}

	}

	@EventHandler
	private void onStartComplete(ServerLoadEvent event) {
		if (!this.plugin.toggleFreeze) {
			return;
		}
		if (this.shouldItBeFrozen(new UUID(0,0))) {
			this.serverTickManager.setFrozen(true);
			this.plugin.logger
					.info("Server is frozen until a player without the noplayerfreeze.ignore permission joins.");
		}
	}
}
