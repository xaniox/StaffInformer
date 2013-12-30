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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.OfflinePlayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.matzefratze123.staffinformer.util.Logger;

public class Settings {
	
	private static final String DATA_FOLDER = "data";
	private static final String FILE_EXTENSION = "xml";
	
	private static final String SETTINGS_ELEMENT = "settings";
	private static final String SHOW_SCOREBOARD_ELEMENT = "showScoreboard";
	
	private String holder;
	private boolean showScoreboard = true;
	
	public Settings(String holder) {
		this.holder = holder;
	}
	
	public boolean getShowScoreboard() {
		return showScoreboard;
	}
	
	public void setShowScoreboard(boolean showScoreboard) {
		this.showScoreboard = showScoreboard;
	}
	
	public String getHolder() {
		return holder;
	}
	
	public void save() {
		try {
			File userFolder = new File(StaffInformer.getInstance().getDataFolder(), DATA_FOLDER);
			userFolder.mkdirs();
			
			File userFile = new File(userFolder, holder + "." + FILE_EXTENSION);
			if (!userFile.exists()) {
				userFile.createNewFile();
			}
			
			//Create document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			//Add root element
			Element rootElement = doc.createElement(SETTINGS_ELEMENT);
			doc.appendChild(rootElement);
			
			//Add settings
			Element showSBElement = doc.createElement(SHOW_SCOREBOARD_ELEMENT);
			showSBElement.appendChild(doc.createTextNode(String.valueOf(showScoreboard)));
			rootElement.appendChild(showSBElement);
			
			//Save file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(userFile);
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (Exception e) {
			Logger.severe("Failed to save settings for user " + holder + ": " + e.getMessage());
		}
	}
	
	public static Settings load(OfflinePlayer player) {
		try {
			File file = new File(StaffInformer.getInstance().getDataFolder(), DATA_FOLDER + "/" + player.getName() + "." + FILE_EXTENSION);
			if (!file.exists()) {
				return null;
			}
			
			Settings settings = new Settings(player.getName());
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			
			NodeList rootNodes = doc.getChildNodes();
			for (int i = 0; i < rootNodes.getLength(); i++) {
				Node item = rootNodes.item(i);
				
				if (item.getNodeName().equalsIgnoreCase(SETTINGS_ELEMENT)) {
					NodeList settingsNodes = item.getChildNodes();
					
					for (int j = 0; j < settingsNodes.getLength(); j++) {
						Node setting = settingsNodes.item(j);
						
						if (setting.getNodeName().equalsIgnoreCase(SHOW_SCOREBOARD_ELEMENT)) {
							settings.setShowScoreboard(Boolean.parseBoolean(setting.getTextContent()));
						}
					}
				}
			}
			
			return settings;
		} catch (Exception e) {
			Logger.severe("Failed to load settings for user " + player.getName() + ": " + e.getMessage());
		}
		
		return null;
	}
	
}
