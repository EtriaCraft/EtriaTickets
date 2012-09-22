package com.etriacraft.plugins.EtriaTickets;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class Utils {
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	public static boolean hasPerm(CommandSender cs, String perm) {
		return cs.hasPermission(perm);
	}
	
	public static void noPermMessage(CommandSender s) {
		s.sendMessage("§cYou don't have permission to do that!");
	}
	
	public static boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) { return false; }
	}
	
	public static int getInt(String string) {
		return Integer.parseInt(string);
	}
	
	public static String buildString(String[] args, int begin) {
		StringBuilder mess = new StringBuilder();
		for (int i = begin; i < args.length; i++) {
			if (i > begin) {
				mess.append(" ");
			}
			mess.append(args[i]);
		}
		return mess.toString().trim();
	}
	
	public static String getStatusChar(String p, String on, String off) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(p);
		return player.isOnline()? on : off;
	}
	
	public static String timeSince(long now, long then) {
		long sec = now - then;
		int buf;
		if (sec < 60*2) {
			buf = Math.round(sec);
			return buf + ".second" + (buf==1?"":"s");
		}
		if (sec < 3600*2) {
			buf = Math.round(sec);
			return buf + " second" + (buf==1?"":"s");
		}
		if (sec < 86400*2) {
			buf = Math.round(sec/3600);
			return buf + " hour" + (buf==1?"":"s");
		}
		buf = Math.round(sec/86400);
		return buf + " day" + (buf==1?"":"s");
	}

}