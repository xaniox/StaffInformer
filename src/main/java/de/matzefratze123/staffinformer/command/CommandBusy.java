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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.matzefratze123.staffinformer.StaffInformer;
import de.matzefratze123.staffinformer.StaffScoreboard;
import de.matzefratze123.staffinformer.util.Permissions;

public class CommandBusy extends HSCommand {
	
	public CommandBusy() {
		setPermission(Permissions.BUSY);
		setOnlyIngame(true);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		StaffScoreboard scoreboard = StaffInformer.getInstance().getScoreboard();
		
		boolean newState = !scoreboard.isBusy(player);
		scoreboard.setBusy(player, newState);
		
		if (newState) {
			player.sendMessage(_("scoreboard.messages.nowBusy"));
		} else {
			player.sendMessage(_("scoreboard.messages.noLongerBusy"));
		}
	}

	@Override
	protected void getHelp(Help help) {
		help.setUsage("/staff busy");
		help.setHelp("Sets your current state to busy, means that your name doesn't show up in the online staff list");
	}

}
