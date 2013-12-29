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

import org.bukkit.ChatColor;

public class Help {

	private String			usage;
	private String          help;

	public Help(HSCommand command) {
		command.getHelp(this);
	}

	public String getUsage() {
		return ChatColor.RED + "Usage: " + usage;
	}

	public String getRawUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getHelp() {
		return help;
	}

}
