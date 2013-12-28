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
import org.bukkit.configuration.file.FileConfiguration;

import de.matzefratze123.staffinformer.StaffInformer;
import de.matzefratze123.staffinformer.util.I18N;
import de.matzefratze123.staffinformer.util.Permissions;

public abstract class HSCommand implements Comparable<HSCommand> {

	private String					name;
	private Permissions				permission	= null;
	private int						minArgs		= 0;
	private boolean					onlyIngame	= false;
	private Help					help;

	public static StaffInformer		plugin;
	public static FileConfiguration	config;
	
	public HSCommand() {
		help = new Help(this);
		getHelp(help);
	}
	
	public abstract void execute(CommandSender sender, String[] args);

	protected abstract void getHelp(Help help);

	public String getName() {
		return name;
	}

	protected HSCommand setName(String name) {
		this.name = name;

		return this;
	}

	public void setMinArgs(int arg) {
		this.minArgs = arg;
	}

	public int getMinArg() {
		return minArgs;
	}

	public void setPermission(Permissions perm) {
		this.permission = perm;
	}

	public Permissions getPermission() {
		return permission;
	}

	public boolean onlyIngame() {
		return onlyIngame;
	}

	public void setOnlyIngame(boolean ingame) {
		this.onlyIngame = ingame;
	}
	
	public Help getHelp() {
		return help;
	}

	public static String _(String path, String... args) {
		return I18N.getMessage(path, args);
	}

	public static String __(String str) {
		return StaffInformer.PREFIX + str;
	}

	public static void setPluginInstance(StaffInformer instance) {
		plugin = instance;
	}

	public static void setFileConfiguration(FileConfiguration c) {
		config = c;
	}
	
	public String getUsage() {
		return help.getUsage();
	}
	
	@Override
	public int compareTo(HSCommand other) {
		return name.compareTo(other.name);
	}

}
