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
