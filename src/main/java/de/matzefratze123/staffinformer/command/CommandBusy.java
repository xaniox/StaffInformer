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
		help.addHelp("Sets your current state to busy, means that your name doesn't show up in the online staff list");
	}

}
