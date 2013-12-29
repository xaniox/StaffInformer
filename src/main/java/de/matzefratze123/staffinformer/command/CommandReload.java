package de.matzefratze123.staffinformer.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import de.matzefratze123.staffinformer.BoardLayout;
import de.matzefratze123.staffinformer.StaffInformer;
import de.matzefratze123.staffinformer.util.I18N;
import de.matzefratze123.staffinformer.util.Logger;
import de.matzefratze123.staffinformer.util.Permissions;

public class CommandReload extends HSCommand {
	
	public CommandReload() {
		setPermission(Permissions.RELOAD);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		I18N.loadMessages();
		
		final File configFile = new File(StaffInformer.getInstance().getDataFolder(), "config.yml");
		final InputStream dcStream = getClass().getResourceAsStream("/defaultconfig.yml");
		
		try {
			StaffInformer.setSystemConfig(YamlConfiguration.loadConfiguration(configFile));
		} catch (Exception e) {
			Logger.severe("Failed to load config! Using default values.");
			StaffInformer.setSystemConfig(YamlConfiguration.loadConfiguration(dcStream));
		} finally {
			try {
				dcStream.close();
			} catch (Exception e) {}
		}
		
		final File layoutFile = new File(StaffInformer.getInstance().getDataFolder(), "layout.xml");
		
		BoardLayout layout;
		
		try {
			layout = BoardLayout.loadFromXml(new FileInputStream(layoutFile), StaffInformer.getSystemConfig().getString("layout"));
		} catch (FileNotFoundException e) {
			//Failed to find file. Load default layout
			layout = BoardLayout.loadFromXml(getClass().getResourceAsStream("/layout.xml"), "default");
		}
		
		StaffInformer.getInstance().getScoreboard().setLayout(layout);
	}

	@Override
	protected void getHelp(Help help) {
		help.setUsage("/staff reload");
		help.setHelp("Reloads the plugin");
	}

}
