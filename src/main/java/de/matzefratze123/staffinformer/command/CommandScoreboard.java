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
package de.matzefratze123.staffinformer.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.matzefratze123.staffinformer.Settings;
import de.matzefratze123.staffinformer.StaffInformer;
import de.matzefratze123.staffinformer.util.Permissions;

public class CommandScoreboard extends HSCommand {
	
	public CommandScoreboard() {
		setOnlyIngame(true);
		setPermission(Permissions.CONTROL_OWN_SCOREBOARD);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Settings settings = StaffInformer.getInstance().getSettings(player);
		
		boolean newState;
		
		if (args.length > 0) {
			newState = args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable");
		} else {
			newState = !settings.getShowScoreboard();
		}
			
		if (settings.getShowScoreboard() && newState) {
			player.sendMessage(_("scoreboard.messages.sbAlreadyEnabled"));
			return;
		}
		
		settings.setShowScoreboard(newState);
		if (newState) {
			StaffInformer.getInstance().getScoreboard().getObjective().showTo(player);
		} else {
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		
		player.sendMessage(_("scoreboard.messages.toggled", newState ? "enabled" : "disabled"));
	}

	@Override
	protected void getHelp(Help help) {
		help.setUsage("/staff scoreboard <on|off>");
		help.addHelp("Disables or your scoreboard for your current session");
	}

}
