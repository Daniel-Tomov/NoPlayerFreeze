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

import com.earth2me.essentials.Essentials;

import net.ess3.api.events.AfkStatusChangeEvent;

public class Events implements Listener {
	private Main plugin;
	private ServerTickManager serverTickManager;
	private String lastPlayerToLeave;

	public Events(Main plugin) {
		this.plugin = plugin;
		this.serverTickManager = Bukkit.getServerTickManager();
		this.lastPlayerToLeave = "";
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
				// player is ignored due to permission
			} else if (this.plugin.essentials.getUser(player).isAfk() && plugin.essentials_support()) {
				// player is afk
			} else {
				counter++; // player did not leave, have permission, or is afk
			}

		}
		return counter == 0;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (!this.plugin.toggleFreeze) {
			return;
		}

		if (event.getPlayer().hasPermission("noplayerfreeze.ignore")) {
			return;
		}

		if (this.shouldItBeFrozen(event.getPlayer().getUniqueId())) {
			this.serverTickManager.setFrozen(true);
			if (this.plugin.debug()) {
				this.plugin.logger.info(
						"A player left. All players in the server have the noplayerfreeze.ignore permission. The server is frozen.");
			}
			this.lastPlayerToLeave = event.getPlayer().getName();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!this.plugin.toggleFreeze) {
			return;
		}
		if (!this.serverTickManager.isFrozen()) {
			return;
		}

		if (event.getPlayer().hasPermission("noplayerfreeze.ignore")) {
			return;
		}

		if (!this.shouldItBeFrozen(new UUID(0, 0))) {
			this.serverTickManager.setFrozen(false);
			if (this.plugin.debug()) {
				this.plugin.logger.info(
						"A player without the noplayerfreeze.ignore permission joined. The server is not frozen.");
			}

		} else {
			this.serverTickManager.setFrozen(true);
			if (this.plugin.debug()) {
				this.plugin.logger.info(
						"A player joined. All players in the server have the noplayerfreeze.ignore permission. The server is frozen.");
			}
		}
	}

	@EventHandler
	private void onStartComplete(ServerLoadEvent event) {
		if (!this.shouldItBeFrozen(new UUID(0, 0))) {
			return;
		}
		this.serverTickManager.setFrozen(true);
		if (this.plugin.debug()) {
			this.plugin.logger
					.info("Server is frozen until a player without the noplayerfreeze.ignore permission joins.");
		}
		this.plugin.essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangeAfkStatus(AfkStatusChangeEvent e) {
		if (!this.plugin.essentials_support()) {
			return;
		}
		if (!this.plugin.toggleFreeze) {
			return;
		}

		if (Bukkit.getPlayer(e.getAffected().getName()).hasPermission("noplayerfreeze.ignore")) {
			return; // ignore AFK status of ignored players
		}

		int counter = 0;
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if (player.hasPermission("noplayerfreeze.ignore")) {
				// player is ignored
			} else if (player.getName() == e.getAffected().getName() && e.getValue()) {
				// player who triggered the event is AFK
			} else if (player.getName() == e.getAffected().getName() && !e.getValue()) {
				// player who triggered the event is not AFK
				counter++;
			} else if (this.plugin.essentials.getUser(player).isAfk()) {
				// the player from the for loop is afk
			} else {
				this.plugin.logger.info(player.getName() + " is not AFK");
				counter++;
			}
		}

		if (counter == 0)

		{
			this.serverTickManager.setFrozen(true);
			if (this.plugin.debug()) {
				this.plugin.logger.info(
						"A player went AFK. All players in the server have the noplayerfreeze.ignore permission or are AFK. The server is frozen.");
			}
		} else if (this.lastPlayerToLeave == e.getAffected().getName()) {
			this.lastPlayerToLeave = "";
			return;
		} else if (this.serverTickManager.isFrozen()) {
			this.serverTickManager.setFrozen(false);
			if (this.plugin.debug()) {
				this.plugin.logger.info("A player has gone non-AFK. Unfreezing the server");
			}
		}
	}
}