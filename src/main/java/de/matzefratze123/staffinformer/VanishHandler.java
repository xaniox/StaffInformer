package de.matzefratze123.staffinformer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishHandler implements Listener {
	
	@EventHandler
	public void onVanishStatusChange(VanishStatusChangeEvent e) {
		Player player = e.getPlayer();
		StaffScoreboard board = StaffInformer.getInstance().getScoreboard();
		
		if (e.isVanishing()) {
			board.refresh(player);
		} else {
			board.refresh();
		}
	}
	
	public static VanishPlugin getVanishPlugin() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("VanishNoPacket");
		
		if (plugin == null) {
			return null;
		}
		if (!plugin.isEnabled()) {
			return null;
		}
		if (!(plugin instanceof VanishPlugin)) {
			return null;
		}
		
		return (VanishPlugin) plugin;
	}
	
	public static boolean isVanished(Player player) {
		if (getVanishPlugin() == null) {
			return false;
		}
		
		return getVanishPlugin().getManager().isVanished(player);
	}
	
}
