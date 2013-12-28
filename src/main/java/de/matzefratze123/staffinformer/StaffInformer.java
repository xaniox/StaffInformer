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
package de.matzefratze123.staffinformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.matzefratze123.staffinformer.command.CommandHandler;
import de.matzefratze123.staffinformer.util.IOUtil;
import de.matzefratze123.staffinformer.util.Logger;

public class StaffInformer extends JavaPlugin implements Listener {
	
	public static final String PREFIX = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "StaffInformer" + ChatColor.DARK_GRAY + ChatColor.BOLD + "] ";
	public static final char CHAR_COLOR_CHAR = '&';
	
	private static StaffInformer instance;
	private static FileConfiguration config;
	
	private StaffScoreboard scoreboard;
	private Map<String, Settings> settingsMap = new HashMap<String, Settings>();
	
	@Override
	public void onEnable() {
		instance = this;
		getDataFolder().mkdirs();
		
		final File configFile = new File(getDataFolder(), "config.yml");
		final InputStream dcStream = getClass().getResourceAsStream("/defaultconfig.yml");
		
		try {
			if (!configFile.exists()) {
				IOUtil.copy(dcStream, configFile);
			}
			
			config = YamlConfiguration.loadConfiguration(configFile);
		} catch (IOException e) {
			Logger.severe("Failed to copy default config! Using default values.");
			config = YamlConfiguration.loadConfiguration(dcStream);
		} finally {
			try {
				dcStream.close();
			} catch (Exception e) {}
		}
		
		final File layoutFile = new File(getDataFolder(), "layout.xml");
		if (!layoutFile.exists()) {
			BoardLayout.copyDefaultLayoutFile();
		}
		
		BoardLayout layout;
		
		try {
			layout = BoardLayout.loadFromXml(new FileInputStream(layoutFile), "default");
		} catch (FileNotFoundException e) {
			//Failed to find file. Load default layout
			layout = BoardLayout.loadFromXml(getClass().getResourceAsStream("/layout.xml"), config.getString("layout"));
		}
		
		scoreboard = new StaffScoreboard(layout);
		
		CommandHandler.initCommands();
		
		getCommand("staff").setExecutor(new CommandHandler());
		
		getServer().getPluginManager().registerEvents(this, this);
		if (VanishHandler.getVanishPlugin() != null) {
			getServer().getPluginManager().registerEvents(new VanishHandler(), this);
		}
		
		Logger.info("StaffInformer v" + getDescription().getVersion() + " enabled!");
	}
	
	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		
		Logger.info("StaffInformer disabled!");
	}
	
	public static StaffInformer getInstance() {
		return instance;
	}
	
	public static FileConfiguration getSystemConfig() {
		return config;
	}
	
	public StaffScoreboard getScoreboard() {
		return scoreboard;
	}
	
	public Settings getSettings(OfflinePlayer player) {
		return settingsMap.get(player.getName());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		Settings settings = Settings.load(player);
		if (settings == null) {
			settings = new Settings(player.getName());
		}
		
		settingsMap.put(player.getName(), settings);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		handleQuit(e);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleQuit(e);
	}
	
	private void handleQuit(PlayerEvent e) {
		Player player = e.getPlayer();
		
		settingsMap.get(player.getName()).save();
		settingsMap.remove(player.getName());
	}
	
}