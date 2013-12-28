package de.matzefratze123.staffinformer.util;

import java.util.logging.Level;

import de.matzefratze123.staffinformer.StaffInformer;

/**
 * Provides an easy class for quick logging
 * 
 * @author matzefratze123
 */
public class Logger {
	
	public static void info(String msg) {
		StaffInformer.getInstance().getLogger().log(Level.INFO, msg);
	}
	
	public static void warning(String msg) {
		StaffInformer.getInstance().getLogger().log(Level.WARNING, msg);
	}
	
	public static void severe(String msg) {
		StaffInformer.getInstance().getLogger().log(Level.SEVERE, msg);
	}
	
}


