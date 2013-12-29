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
package de.matzefratze123.staffinformer.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import de.matzefratze123.staffinformer.StaffInformer;

public class WrappedInformationObjective {
	
	private static final int MAX_LINES = 15;
	private static final String DUMMY_CRITERIA = "dummy";
	
	/**
	 * A list of color-codes to fake an empty line
	 */
	private static final String[] EMPTY_TABLE = {
		"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7",
		"§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f",
		"§k", "§l", "§m", "§n", "§o", "§r"
	};
	
	private Objective objective;
	private List<String> lines;
	
	private List<RollingText> rollingTextTasks;
	
	public WrappedInformationObjective(Objective objective) {
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		this.objective = objective;
		this.lines = new ArrayList<String>();
		this.rollingTextTasks = new ArrayList<RollingText>();
		
		for (int i = 0; i < MAX_LINES; i++) {
			lines.add(null);
		}
	}
	
	public WrappedInformationObjective(Scoreboard scoreboard, String objectiveName) {
		this(scoreboard.registerNewObjective(objectiveName, DUMMY_CRITERIA));
	}
	
	public void setDisplayName(String name) {
		objective.setDisplayName(name);
	}
	
	public List<String> getItems() {
		List<String> list = new ArrayList<String>();
		
		for (String item : lines) {
			if (item == null) {
				continue;
			}
			
			list.add(item);
		}
		
		return list;
	}
	
	public boolean containsItem(String str) {
		if (str == null) {
			return false;
		}
		
		return lines.contains(str);
	}
	
	public void clear() {
		for (int i = 0; i < MAX_LINES; i++) {
			setLine(i, null);
		}
	}
	
	public void showTo(Player player) {
		player.setScoreboard(objective.getScoreboard());
	}
	
	public void setLine(int lineIndex, String text) {
		setLine(lineIndex, text, true);
	}
	
	private void setLine(int lineIndex, String text, boolean stopTask) {
		Validate.isTrue(lineIndex < 15, "lineIndex greater than 14 (" + lineIndex + ")");
		Validate.isTrue(lineIndex >= 0, "lineIndex less than 0 (" + lineIndex + ")");
		Validate.isTrue(!containsItem(text), "double line value " + text);
		
		for (String spacing : EMPTY_TABLE) {
			if (text == null) {
				continue;
			}
			
			if (text.equalsIgnoreCase(spacing)) {
				throw new IllegalArgumentException(text + " cannot be a single color code");
			}
		}
		
		String oldItem = lines.get(lineIndex);
		if (oldItem != null) {
			resetScore(oldItem);
		}
		
		//Check tasks
		if (stopTask) {
			for (RollingText task : rollingTextTasks) {
				if (task.getLineIndex() == lineIndex) {
					task.cancel();
				}
			}
		}
		
		if (text != null && text.length() > RollingText.MAX_CHARS) {
			RollingText task = new RollingText(lineIndex, text);
			task.runTaskTimer(StaffInformer.getInstance(), 0L, 5L);
			
			rollingTextTasks.add(task);
		} else {
			lines.set(lineIndex, text);
		}
		
		refresh();
	}
	
	private void refresh() {
		boolean insertSpacings = false;
		int spacingIndex = 0;
		
		for (String spacing : EMPTY_TABLE) {
			resetScore(spacing);
		}
		
		for (int i = MAX_LINES - 1; i >= 0; i--) {
			String item = lines.get(i);
			String line;
			
			if (item == null) {
				if (!insertSpacings) {
					continue;
				}
				
				line = EMPTY_TABLE[spacingIndex];
				spacingIndex++;
			} else {
				insertSpacings |= item != null;
				line = item;
			}
			
			Score score = objective.getScore(Bukkit.getOfflinePlayer(line));
			score.setScore(MAX_LINES - i);
		}
	}
	
	private void resetScore(String s) {
		objective.getScoreboard().resetScores(Bukkit.getOfflinePlayer(s));
	}
	
	private class RollingText extends BukkitRunnable {
		
		private static final int MAX_CHARS = 16;
		private static final String MESSAGE_SPLITTER = "  ";
		
		private int lineIndex;
		private String text;
		
		private int currentIndex;
		
		public RollingText(int lineIndex, String text) {
			this.lineIndex = lineIndex;
			this.text = text + MESSAGE_SPLITTER;
		}
		
		@Override
		public void run() {
			String currentPart;
			
			int endIndex = currentIndex + 16;
			if (endIndex > text.length()) {
				currentPart = text.substring(currentIndex, text.length());
				String startPart = text.substring(0, MAX_CHARS - currentPart.length());
				
				currentPart = currentPart + startPart;
			} else {
				currentPart = text.substring(currentIndex, currentIndex + 16);
			}
			
			setLine(lineIndex, currentPart, false);
			
			currentIndex = ++currentIndex % text.length();
		}
		
		public int getLineIndex() {
			return lineIndex;
		}
		
	}
	
}