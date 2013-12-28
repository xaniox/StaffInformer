package de.matzefratze123.staffinformer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import de.matzefratze123.staffinformer.StaffInformer;

public class CommandVersion extends HSCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		PluginDescriptionFile pdf = StaffInformer.getInstance().getDescription();
		
		sender.sendMessage(pdf.getName() + " v" + pdf.getVersion() + " by " + pdf.getAuthors().get(0));
		//sender.sendMessage("BukkitDevPage");
	}

	@Override
	protected void getHelp(Help help) {
		help.setUsage("/staff version");
		help.addHelp("Shows plugin version and information");
	}
	
}
