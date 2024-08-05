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
import org.bukkit.event.server.ServerLoadEvent;

import com.earth2me.essentials.Essentials;

import net.ess3.api.events.AfkStatusChangeEvent;

public class Events implements Listener {
	Main plugin;
	private ServerTickManager serverTickManager;

	public Events(Main plugin) {
		this.plugin = plugin;
		this.serverTickManager = Bukkit.getServerTickManager();
	}

	// returns true if all players have ignore permission or are AFK
	private boolean shouldItBeFrozen() {
		if (!plugin.toggleFreeze) {
			return false;
		}

		int counter = 0;
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {

			if (player.hasPermission("noplayerfreeze.ignore")) {
				// hello
			} else if (plugin.essentials.getUser(player).isAfk() && plugin.essentials_support()) {
				counter++;
			} else if (!plugin.essentials.getUser(player).isAfk() && plugin.essentials_support()) {
				// AFK status is after the event finishes
			} else {
				counter++;
			}

		}
		return counter == 0;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (this.serverTickManager.isFrozen()) {
			return;
		}
		if (shouldItBeFrozen() || Bukkit.getOnlinePlayers().size() == 1) {
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
		if (!this.serverTickManager.isFrozen()) {
			return;
		}
		if (!event.getPlayer().hasPermission("noplayerfreeze.ignore")) {
			this.serverTickManager.setFrozen(false);
			Bukkit.getLogger()
					.info("A player without the noplayerfreeze.ignore permission joined. The server is not frozen.");

		}
	}

	@EventHandler
	private void onStartComplete(ServerLoadEvent event) {
		if (!shouldItBeFrozen()) {
			return;
		}
		this.serverTickManager.setFrozen(true);
		this.plugin.logger.info("Server is frozen until a player without the noplayerfreeze.ignore permission joins.");
		plugin.essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

	}

	@EventHandler
	public void onPlayerChangeAfkStatus(AfkStatusChangeEvent e) {
		if (!plugin.essentials_support()) {
			return;
		}
		if (!plugin.toggleFreeze) {
			return;
		}

		if (shouldItBeFrozen()) {
			this.serverTickManager.setFrozen(true);
			this.plugin.logger.info(
					"All players in the server have the noplayerfreeze.ignore permission or are AFK. The server is frozen.");

		} else {
			this.serverTickManager.setFrozen(false);
			this.plugin.logger.info("A player has gone non-AFK. Unfreezing the server");
		}
	}
}
