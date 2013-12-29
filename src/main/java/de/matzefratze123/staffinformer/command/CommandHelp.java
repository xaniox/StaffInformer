package de.matzefratze123.staffinformer.command;

import org.bukkit.command.CommandSender;

public class CommandHelp extends HSCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		CommandHandler.sendHelp(sender);
	}

	@Override
	protected void getHelp(Help help) {
		help.setUsage("/staff help");
		help.setHelp("Shows the help");
	}

}
