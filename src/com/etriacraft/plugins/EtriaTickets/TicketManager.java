package com.etriacraft.plugins.EtriaTickets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TicketManager {
	
	public static SortedMap<Integer, Ticket> tickets = new TreeMap<Integer, Ticket>();
	
	public static void loadTickets() {
		tickets.clear();
		ResultSet rs = Main.db.sql.readQuery("SELECT * from `tickets` ORDER BY `id` ASC");
		
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String creator = rs.getString("creator");
				String message = rs.getString("message");
				long timestamp = rs.getLong("timestamp");
				String comment = rs.getString("comment");
				String user_comment = rs.getString("user_comment");
				World world = Bukkit.getWorld(rs.getString("world"));
				
				if (world == null) continue;
				
				Location loc = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
				boolean held = rs.getBoolean("held");
				boolean completed = rs.getBoolean("completed");
				String assignee = rs.getString("assignee");
				
				Ticket ticket = new Ticket(id, creator, message, timestamp, loc, held, completed);
				
				if (comment != null) ticket.setComment(comment);
				if (user_comment != null) ticket.setCreatorComment(user_comment);
				if (assignee != null) ticket.assign(assignee);
				
				TicketManager.tickets.put(id, ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int createTicket(Player user, String message) {
		String creator = user.getName();
		long timestamp = (System.currentTimeMillis() / 1000L);
		Location loc = user.getLocation();
		int index;
		
		try {
			PreparedStatement stmt = Main.db.conn.prepareStatement("INSERT INTO `tickets` (`creator`, `message`, `timestamp`, `world`, `x`, `y`, `z`) VALUES(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			
			// Set Arguments for Statement
			stmt.setString(1, creator);
			stmt.setString(1, message);
			stmt.setLong(3, timestamp);
			stmt.setString(4, loc.getWorld().getName());
			stmt.setInt(5, (int) loc.getX());
			stmt.setInt(6, (int) loc.getY());
			stmt.setInt(7, (int) loc.getZ());
			
			// Execute
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			// Move to first row
			rs.next();
			index = rs.getInt(1);
			
			stmt.close();
			rs.close();
			
			// Creates ticket, adds to memory.
			tickets.put(index, new Ticket(index, creator, message, timestamp, loc, false, false));
			return index;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void assignTicket(Ticket ticket, String player) {
		ticket.assign(player);
		Main.db.sql.modifyQuery("UPDATE `tickets` SET `assignee` = '" + player + "' WHERE `id` = " + ticket.getId());
	}
	
	public static void unassignTicket(Ticket ticket) {
        ticket.assign(null);
        Main.db.sql.modifyQuery("UPDATE `tickets` SET `assignee` = NULL WHERE `id` = " + ticket.getId());
    }
    
    public static void holdTicket(Ticket ticket) {
        ticket.hold();
        Main.db.sql.modifyQuery("UPDATE `tickets` SET `held` = "+ 1 +" WHERE `id` = " + ticket.getId());
    }
    
    public static void unHoldTicket(Ticket ticket) { 
        ticket.unHold();
        Main.db.sql.modifyQuery("UPDATE `tickets` SET `held` = "+ 0 +" WHERE `id` = " + ticket.getId());
    }
    
    public static void closeTicket(Ticket ticket) { 
        ticket.complete();
        Main.db.sql.modifyQuery("UPDATE `tickets` SET `completed` = "+ 1 +" WHERE `id` = " + ticket.getId());
    }
    
    public static void openTicket(Ticket ticket) {
        ticket.unComplete();
        Main.db.sql.modifyQuery("UPDATE `tickets` SET `completed` = "+ 0 +" WHERE `id` = " + ticket.getId());
    }

    
    public static void setTicketComment(Ticket ticket, String message, boolean mod) {
        String query;
        if (mod) {
            ticket.setComment(message);
            query = "UPDATE `tickets` SET `comment` = ? WHERE `id` = ?;";
        } else {
            ticket.setCreatorComment(message);
            query = "UPDATE `tickets` SET `user_comment` = ? WHERE `id` = ?;";
        }
        
        try {
            PreparedStatement stmt = Main.db.conn.prepareStatement(query);
            
            stmt.setString(1, message);
            stmt.setInt(2, ticket.getId());
            
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Ticket getTicket(int id) {
        try {
            return tickets.get(id);
        } catch (IndexOutOfBoundsException e) { return null; }
    }
    
    
    public static List<Ticket> getOwnTickets(Player p) {
        List<Ticket> ticks = new ArrayList<Ticket>();
        for (Integer i : tickets.keySet()) {
            Ticket ticket = tickets.get(i);
            
            if (ticket.isOpen() && ticket.getCreator().equals(p.getName())) {
                ticks.add(ticket);
            } 
        }
        
        return ticks;
    }
    
    public static List<Ticket> getOpenTickets() {
        List<Ticket> ticks = new ArrayList<Ticket>();
        for (Integer i : tickets.keySet()) {
            Ticket ticket = tickets.get(i);
            
            if (ticket.isOpen()) {
                ticks.add(ticket);
            } 
        }
        
        return ticks;
    }
    
    public static List<Ticket> getHeldTickets() {
        List<Ticket> ticks = new ArrayList<Ticket>();
        for (Integer i : tickets.keySet()) {
            Ticket ticket = tickets.get(i);
            
            if (ticket.isHeld()) {
                ticks.add(ticket);
            } 
        }
        
        return ticks;
    }

}