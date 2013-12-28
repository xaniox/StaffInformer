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
