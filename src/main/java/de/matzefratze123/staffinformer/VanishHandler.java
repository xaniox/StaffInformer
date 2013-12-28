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
