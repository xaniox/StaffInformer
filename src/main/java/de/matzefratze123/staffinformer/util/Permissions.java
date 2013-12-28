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
package de.matzefratze123.staffinformer.util;

public enum Permissions {
	
	STAFF("staff"),
	BUSY("busy"), 
	SHOW_SCOREBOARD("show"),
	CONTROL_OWN_SCOREBOARD("controlownscoreboard");
	
	private static final String BASE_PERMISSION = "staffinformer";
	
	private String topPermission;
	
	private Permissions(String topPermission) {
		this.topPermission = topPermission;
	}
	
	public String getTopPermission() {
		return topPermission;
	}
	
	public String get() {
		return BASE_PERMISSION + "." + topPermission;
	}
	
	@Override
	public String toString() {
		return get();
	}
	
}
