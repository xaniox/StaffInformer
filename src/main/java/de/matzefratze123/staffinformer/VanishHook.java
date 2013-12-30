/**
 *   StaffInformer - Inform players about online staff
 *   
 *   Copyright (C) 2013-2014 matzefratze123
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package de.matzefratze123.staffinformer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishHook implements Listener {
	
	private static final String PLUGIN_NAME = "VanishNoPacket";
	private VanishPlugin plugin;
	
	public VanishHook() {
		if (hasHook()) {
			hook();
			Bukkit.getPluginManager().registerEvents(this, StaffInformer.getInstance());
		}
	}
	
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
	
	public void hook() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		
		if (plugin == null) {
			return;
		}
		if (!plugin.isEnabled()) {
			return;
		}
		if (!(plugin instanceof VanishPlugin)) {
			return;
		}
		
		this.plugin = (VanishPlugin) plugin;
	}
	
	public boolean hasHook() {
		if (this.plugin != null) {
			return true;
		}
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		
		if (plugin == null) {
			return false;
		}
		if (!plugin.isEnabled()) {
			return false;
		}
		if (!(plugin instanceof VanishPlugin)) {
			return false;
		}
		
		return true;
	}
	
	public VanishPlugin getHook() {
		return plugin;
	}
	
	public boolean isVanished(Player player) {
		if (plugin == null) {
			throw new IllegalStateException("Hook is not initialized!");
		}
		
		return plugin.getManager().isVanished(player);
	}
	
}
