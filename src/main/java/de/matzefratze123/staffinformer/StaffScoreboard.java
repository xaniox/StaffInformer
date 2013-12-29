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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import de.matzefratze123.staffinformer.util.I18N;
import de.matzefratze123.staffinformer.util.Permissions;
import de.matzefratze123.staffinformer.wrapper.WrappedInformationObjective;

public class StaffScoreboard implements Listener {
	
	private static final String OBJECTIVE_NAME = "showStaff";
	
	private WrappedInformationObjective objective;
	private Scoreboard scoreboard;
	private BoardLayout layout;
	private List<String> busy;
	
	public StaffScoreboard(BoardLayout layout) {
		this.layout = layout;
		
		this.busy = new ArrayList<String>();
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = new WrappedInformationObjective(scoreboard, OBJECTIVE_NAME);
		this.objective.setDisplayName(I18N.getMessage("scoreboard.scoreboard.title")); 
		
		Bukkit.getPluginManager().registerEvents(this, StaffInformer.getInstance());
	}
	
	public Scoreboard getBukkitScoreboard() {
		return scoreboard;
	}
	
	public WrappedInformationObjective getObjective() {
		return objective;
	}
	
	public BoardLayout getLayout() {
		return layout;
	}
	
	public void setLayout(BoardLayout layout) {
		this.layout = layout;
	}
	
	public void refresh() {
		refresh(null);
	}
	
	public void refresh(Player ignoring) {
		layout.generate(this, ignoring);
	}
	
	public void setBusy(Player player, boolean state) {
		if (state) {
			if (busy.contains(player.getName())) {
				return;
			}
			
			busy.add(player.getName());
		} else {
			busy.remove(player.getName());
		}
		
		refresh();
	}
	
	public boolean isBusy(Player player) {
		return busy.contains(player.getName());
	}
	
	public List<String> getBusys() {
		return busy;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		refresh();
		
		final Player player = e.getPlayer();
		
		if (!player.hasPermission(Permissions.SHOW_SCOREBOARD.get())) {
			return;
		}
		if (!StaffInformer.getInstance().getSettings(player).getShowScoreboard()) {
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(StaffInformer.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (!player.isOnline()) {
					return;
				}
				
				objective.showTo(e.getPlayer());
			}
		}, 15L);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		refresh(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		refresh(e.getPlayer());
	}
	
}
