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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.matzefratze123.staffinformer.StaffInformer;
import de.matzefratze123.staffinformer.util.I18N;

public class CommandHandler implements CommandExecutor {
	
	public static Map<String, HSCommand> commands = new HashMap<String, HSCommand>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sendHelp(sender);
			return true;
		}
		
		HSCommand command = getSubCommand(args[0]);
		if (command == null) {
			sender.sendMessage(I18N.getMessage("unknownCommand"));
			return true;
		}
		if (args.length > 1 && (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("?"))) {
			Help help = command.getHelp();
			sender.sendMessage(ChatColor.YELLOW + "Usage: " + help.getRawUsage());
			sender.sendMessage(ChatColor.YELLOW + help.getHelp());
			
			return true;
		}
		
		Vector<String> cutArgs = new Vector<String>(Arrays.asList(args));
		cutArgs.remove(0);
		
		if (!isValidSubCommand(sender, command, cutArgs.toArray(new String[cutArgs.size()])))
			return true;
		
		command.execute(sender, cutArgs.toArray(new String[cutArgs.size()]));
		return true;
	}

	public static boolean isValidSubCommand(CommandSender sender, HSCommand cmd, String[] args) {
		if (cmd.onlyIngame() && !(sender instanceof Player)) {
			sender.sendMessage(I18N.getMessage("onlyIngame"));
			return false;
		}
		
		if (cmd.getPermission() != null && !sender.hasPermission(cmd.getPermission().get()) && !sender.hasPermission("heavyspleef.*")) {
			sender.sendMessage(I18N.getMessage("noPermission"));
			return false;
		}
		
		if (args.length >= cmd.getMinArg()) {
			return true;
		} else {
			Help help = new Help(cmd);
			
			sender.sendMessage(help.getUsage());
		}
		
		return false;
	}
	
	public static void addSubCommand(String name, HSCommand cmd) {
		commands.put(name, cmd);
		
		cmd.setName(name);
	}
	
	public static HSCommand getSubCommand(String name) {
		return commands.get(name.toLowerCase());
	}
	
	public static Map<String, HSCommand> getCommands() {
		return commands;
	}
	
	public static void initCommands() {
		//Add all commands
		addSubCommand("busy", new CommandBusy());
		addSubCommand("b", new CommandBusy());
		addSubCommand("version", new CommandVersion());
		addSubCommand("scoreboard", new CommandScoreboard());
		addSubCommand("board", new CommandScoreboard());
		addSubCommand("sb", new CommandScoreboard());
		addSubCommand("reload", new CommandReload());
		addSubCommand("help", new CommandHelp());
	}
	
	static void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "Staff commands:");
		
		for (Entry<String, HSCommand> entry : commands.entrySet()) {
			HSCommand cmd = entry.getValue();
			
			Help help = cmd.getHelp();
			sender.sendMessage(ChatColor.YELLOW + help.getRawUsage() + " - " + help.getHelp());
		}
	}
	
	public static void setPluginInstance(StaffInformer instance) {
		HSCommand.setPluginInstance(instance);
	}
	
	public static void setConfigInstance(StaffInformer plugin) {
		HSCommand.setFileConfiguration(plugin.getConfig());
	}
}
