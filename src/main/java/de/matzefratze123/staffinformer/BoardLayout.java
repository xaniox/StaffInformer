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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.matzefratze123.staffinformer.util.I18N;
import de.matzefratze123.staffinformer.util.IOUtil;
import de.matzefratze123.staffinformer.util.Logger;
import de.matzefratze123.staffinformer.util.Permissions;

public class BoardLayout {
	
	private static final String ROOT_ELEMENT = "scoreboard-layout";
	private static final String TITLE_ELEMENT = "title";
	private static final String LINE_ELEMENT = "line";
	private static final String ID_ATTRIBUTE = "id";
	
	private static final String ACTIVE_COUNT_VARIABLE = "%active-count%";
	private static final String ACTIVE_VARIABLE = "%active%";
	private static final String BUSY_COUNT_VARIABLE = "%busy-count%";
	private static final String BUSY_VARIABLE = "%busy%";
	private static final String EMPTY_LINE_VARIABLE = "%empty%";
	
	private String title;
	private String[] lines;
	
	public BoardLayout(String title, String[] lines) {
		this.title = ChatColor.translateAlternateColorCodes(StaffInformer.CHAR_COLOR_CHAR, title);;
		this.lines = lines;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = ChatColor.translateAlternateColorCodes(StaffInformer.CHAR_COLOR_CHAR, title);
	}

	public String[] getLines() {
		return lines;
	}

	public void setLines(String[] lines) {
		this.lines = lines;
	}
	
	public void generate(StaffScoreboard board, Player ignoring) {
		board.getObjective().clear();
		board.getObjective().setDisplayName(title);
		
		List<Player> active = getActive(board);
		List<Player> busy = getBusys(board);
		
		int index = 0;
		
		for (String line : lines) {
			if (index > 15) {
				break;
			}
			
			if (line.equalsIgnoreCase(ACTIVE_VARIABLE)) {
				if (active.size() == 0) {
					board.getObjective().setLine(index, I18N.getMessage("scoreboard.scoreboard.noStaff"));
					index++;
				} else {
					for (Player player : active) {
						if (player == ignoring)  {
							continue;
						}
						if (index > 15) {
							return;
						}
						
						board.getObjective().setLine(index, player.getName());
						index++;
					}
				}
			} else if (line.equalsIgnoreCase(BUSY_VARIABLE)) {
				if (busy.size() == 0) {
					board.getObjective().setLine(index, "None");
					index++;
				} else {
					for (Player player : busy) {
						if (player == ignoring) {
							continue;
						}
						if (index > 15) {
							return;
						}
						
						board.getObjective().setLine(index, player.getName());
						index++;
					}
				}
			} else {
				line = line.replace(ACTIVE_COUNT_VARIABLE, String.valueOf(active.size()));
				line = line.replace(BUSY_COUNT_VARIABLE, String.valueOf(busy.size()));
				line = ChatColor.translateAlternateColorCodes(StaffInformer.CHAR_COLOR_CHAR, line);
				
				if (!line.toLowerCase().contains(EMPTY_LINE_VARIABLE)) {
					board.getObjective().setLine(index, line);
				}
				index++;
			}
		}
	}

	private static List<Player> getActive(StaffScoreboard board) {
		List<Player> list = new ArrayList<Player>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.hasPermission(Permissions.STAFF.get())) {
				continue;
			}
			if (StaffInformer.getInstance().getVanishHook().hasHook() && StaffInformer.getInstance().getVanishHook().isVanished(player)) {
				continue;
			}
			if (board.isBusy(player)) {
				continue;
			}
			if (StaffInformer.getInstance().getAFKDetector().isAfk(player)) {
				continue;
			}

			list.add(player);
		}

		return list;
	}

	private static List<Player> getBusys(StaffScoreboard board) {
		List<Player> list = new ArrayList<Player>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.hasPermission(Permissions.STAFF.get())) {
				continue;
			}
			if (StaffInformer.getInstance().getVanishHook().hasHook() && StaffInformer.getInstance().getVanishHook().isVanished(player)) {
				continue;
			}
			if (!board.isBusy(player) && !StaffInformer.getInstance().getAFKDetector().isAfk(player)) {
				continue;
			}
			
			list.add(player);
		}

		return list;
	}

	public static BoardLayout loadFromXml(InputStream xml, String id) {
		String title = "Online Staff";
		String[] lines = new String[0];
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			
			NodeList rootNodes = doc.getChildNodes();
			
			for (int i = 0; i < rootNodes.getLength(); i++) {
				Node rootNode = rootNodes.item(i);
				if (!rootNode.getNodeName().equalsIgnoreCase(ROOT_ELEMENT)) {
					continue;
				}
				
				NamedNodeMap attributes = rootNode.getAttributes();
				Node idAttribute = attributes.getNamedItem(ID_ATTRIBUTE);
				
				if (idAttribute == null) {
					continue;
				}
				
				if (!idAttribute.getNodeValue().equalsIgnoreCase(id)) {
					continue;
				}
				
				NodeList childNodes = rootNode.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node node = childNodes.item(j);
					
					if (node.getNodeName().equalsIgnoreCase(TITLE_ELEMENT)) {
						title = node.getTextContent();
					} else if (node.getNodeName().equalsIgnoreCase(LINE_ELEMENT)) {
						String[] tmpLines = new String[lines.length + 1];
						System.arraycopy(lines, 0, tmpLines, 0, lines.length);
						tmpLines[lines.length] = node.getTextContent();
						
						lines = tmpLines;
					}
				}
			}
			
			return new BoardLayout(title, lines);
		} catch (Exception e) {
			Logger.severe("Failed to load boardlayout " + id + ": " + e.getMessage());
		}
		
		return null;
	}
	
	static void copyDefaultLayoutFile() {
		final File layoutFile = new File(StaffInformer.getInstance().getDataFolder(), "layout.xml");
		final InputStream inStream = BoardLayout.class.getResourceAsStream("/layout.xml");
		
		try {
			IOUtil.copy(inStream, layoutFile);
		} catch (IOException e) {
			Logger.severe("Failed to copy default layout file. Using default layout...");
		}
	}
	
}
