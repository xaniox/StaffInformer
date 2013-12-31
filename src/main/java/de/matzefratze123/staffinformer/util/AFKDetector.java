package de.matzefratze123.staffinformer.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class AFKDetector implements Runnable, Listener {
	
	private static final long     PERIOD         = 20L;
	
	private Plugin                plugin;
	private boolean               started;
	private int                   pid;
	private int                   afkAfter;
	
	private Map<String, Location> lastLocations;
	private Map<String, Integer>  secondsAfk;
	
	public AFKDetector(Plugin plugin, int afkAfter) {
		this.plugin = plugin;
		this.pid = -1;
		this.afkAfter = afkAfter;
		this.lastLocations = new HashMap<String, Location>();
		this.secondsAfk = new HashMap<String, Integer>();
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public int getAfkAfter() {
		return afkAfter;
	}
	
	public void setAfkAfter(int afkAfter) {
		this.afkAfter = afkAfter;
	}
	
	public void startDetector() {
		if (started) {
			throw new IllegalStateException("Task already started");
		}
		
		this.pid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, PERIOD);
		
		started = true;
	}
	
	public void cancelDetector() {
		if (pid == -1) {
			throw new IllegalStateException("Task not running");
		}
		
		Bukkit.getScheduler().cancelTask(pid);
		started = false;
		pid = -1;
	}
	
	public boolean isAfk(Player player) {
		return secondsAfk.containsKey(player.getName()) && secondsAfk.get(player.getName()) >= afkAfter;
	}
	
	public int getSecondsAfk(Player player) {
		int seconds = 0;
		
		if (secondsAfk.containsKey(player.getName())) {
			seconds = secondsAfk.get(player.getName());
		}
		
		return seconds;
	}
	
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!lastLocations.containsKey(player.getName())) {
				lastLocations.put(player.getName(), player.getLocation());
				continue;
			}
			
			Location lastLocation = lastLocations.get(player.getName());
			Location currentLocation = player.getLocation();
			
			int newValue = -1;
			boolean call = false;
			boolean isGoingAfk = false;
			
			if (compareLocations(currentLocation, lastLocation)) {
				
				
				if (secondsAfk.containsKey(player.getName())) {
					newValue = secondsAfk.get(player.getName()).intValue();
					newValue++;
				} else {
					newValue = 1;
				}
				
				secondsAfk.put(player.getName(), newValue);
				
				if (newValue == afkAfter) {
					call = true;
					isGoingAfk = true;
				}
			} else {
				if (secondsAfk.containsKey(player.getName())) {
					int oldValue = secondsAfk.get(player.getName());
					
					if (oldValue >= afkAfter) {
						//Player is going to move
						call = true;
						isGoingAfk = false;
					}
				}
				
				secondsAfk.remove(player.getName());
			}
			
			if (call) {
				PlayerAfkEvent event = new PlayerAfkEvent(player, isGoingAfk);
				Bukkit.getPluginManager().callEvent(event);
			}
			
			lastLocations.put(player.getName(), player.getLocation());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		handleQuit(e);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleQuit(e);
	}
	
	private void handleQuit(PlayerEvent e) {
		Player player = e.getPlayer();
		
		if (secondsAfk.containsKey(player.getName())) {
			secondsAfk.remove(player.getName());
		}
	}
	
	private boolean compareLocations(Location l1, Location l2) {
		if (l1.getBlockX() != l2.getBlockX())
			return false;
		if (l1.getBlockY() != l2.getBlockY())
			return false;
		if (l1.getBlockZ() != l2.getBlockZ())
			return false;
		
		return true;
	}
	
	public static class PlayerAfkEvent extends PlayerEvent {
		
		private static final HandlerList handlers = new HandlerList();
		
		private boolean isGoingAfk;
		
		public PlayerAfkEvent(Player player, boolean isGoingAfk) {
			super(player);
			
			this.isGoingAfk = isGoingAfk;
		}
		
		public boolean isGoingAfk() {
			return isGoingAfk;
		}

		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList() {
			return handlers;
		}
		
	}
	
}
