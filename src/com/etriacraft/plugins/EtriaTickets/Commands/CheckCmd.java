package com.etriacraft.plugins.EtriaTickets.Commands;

import java.text.SimpleDateFormat;
import java.util.List;
import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckCmd implements CommandExecutor {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yy HH:mm");
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		
		if (args.length >= 1) {
			if (Utils.isInt(args[0])) {
				Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
				
				if (ticket != null) {
					if (Utils.hasPerm(s, "EtriaTickets.check.all") || (ticket.getCreator().equals(s.getName()) && Utils.hasPerm(s, "EtriaTickets.check.own"))) {
						showTicket(ticket, s);
					}
				} else {
					s.sendMessage("§cTicket does not exist, try another ID?");
				}
			} else if (args[0].equalsIgnoreCase("page")) {
				if (args.length < 2) return false;
				int page;
				
				if (Utils.isInt(args[1])) page = Utils.getInt(args[1]);
				else {
					s.sendMessage("§cPage number is invalid");
					return true;
				}
				
				if (Utils.hasPerm(s, "EtriaTickets.check.all")) {
					showTicketList(TicketManager.getOpenTickets(), page, s);
				} else if (Utils.hasPerm(s,  "EtriaTickets.check.own")) {
					showTicketList(TicketManager.getOwnTickets((Player) s), page, s);
				} else {
					Utils.noPermMessage(s);
				}
			} else if (args[0].equalsIgnoreCase("held")) {
				if (!Utils.hasPerm(s, "EtriaTickets.check.all")) {
					Utils.noPermMessage(s);
				} else if (args.length > 1) {
					int page;
					if (Utils.isInt(args[1])) page = Utils.getInt(args[1]);
					else {
						s.sendMessage("§cPage number is invalid");
						return true;
					}
					
					showTicketList(TicketManager.getHeldTickets(), page, s);
				} else {
					showTicketList(TicketManager.getHeldTickets(), 1, s);
				}
			} else {
				s.sendMessage("§cInvalid argument!");
			}
			return true;
		} else {
			if (Utils.hasPerm(s,  "EtriaTickets.check.all")) {
				showTicketList(TicketManager.getOpenTickets(), 1, s);
			} else if (Utils.hasPerm(s, "EtriaTickets.check.own")) {
				showTicketList(TicketManager.getOwnTickets((Player) s), 1, s);
			} else {
				Utils.noPermMessage(s);
			}
			return true;
		}
	}
	
	
	public void showTicketList(List<Ticket> tickets, int page, CommandSender s) {
		int maxPages = (tickets.size() + 5 - 1) / 5;
		
		if (tickets.size() < 1) {
			s.sendMessage("§cNo tickets to show.");
			return;
		}
		
		if (page > maxPages || page < 0) {
			s.sendMessage("§cUnknown page");
			return;
		}
		
		s.sendMessage("§6----§e " + tickets.size() + " §6Tickets -------- Page§e " + page + "/" + maxPages + " §6----");
        page--;
        for(int i = 5 * page; (i < 5 * page + 5) && (i < tickets.size()); i++) {
            Ticket ticket = tickets.get(i);
            String message = "§6" + ticket.getId() + " §6| ";
            message += Utils.getStatusChar(ticket.getCreator(), "§a", "§c");
            message += ticket.getCreator() + " §6| ";
            message += Utils.timeSince(System.currentTimeMillis()/1000, ticket.getTimeStamp()) + " |§7 ";
            if (ticket.getAssignee() != null) {
                message += "§6{" + Utils.getStatusChar(ticket.getAssignee(), "§a", "§c") + ticket.getAssignee() + "§6} |§7 ";
            }
            int leftChars = (65 - message.length());
            if (ticket.getMessage().length() < leftChars) {
                message += ticket.getMessage();
            } else {
                if (leftChars < 0) {
                    message += "...";
                } else {
                    message += ticket.getMessage().substring(0, leftChars) + "...";
                }
            }
            s.sendMessage(message);
        }
    }
    
    public void showTicket(Ticket ticket, CommandSender s) {
        String header = "§6---- ID:§e " + ticket.getId() + " §6| Status: " + ticket.getStatusString() + " §6----";
        s.sendMessage(header);
        s.sendMessage("§6Creator: " + Utils.getStatusChar(ticket.getCreator(), "§a", "§c") + ticket.getCreator());
        s.sendMessage("§6Created on:§e " + sdf.format(new java.util.Date(ticket.getTimeStamp() * 1000)));
        if (ticket.getAssignee() != null) s.sendMessage("§6Claimed by: " + Utils.getStatusChar(ticket.getAssignee(), "§a", "§c") + ticket.getAssignee());
        s.sendMessage("§6Message:§7 " + ticket.getMessage());
        if (ticket.getCreatorComment() != null) s.sendMessage("§6Creator-Comment:§7 " + ticket.getCreatorComment());
        if (ticket.getComment() != null) s.sendMessage("§6Mod-Comment:§7 " + ticket.getComment());
    }
}