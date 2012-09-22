package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (!Utils.hasPerm(s, "EtriaTickets.claim")) {
			Utils.noPermMessage(s);
			return true;
		}
		
		if (args.length < 1) return false;
		
		if (!Utils.isInt(args[0])) {
			s.sendMessage("§cInvalid Ticket ID");
			return true;
		}
		
		Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
		
		if (ticket != null) {
			if (!ticket.isOpen()) {
				s.sendMessage("§cYou can't claim a ticket that isn't open.");
			} else if (ticket.getAssignee() == null) {
				TicketManager.assignTicket(ticket, s.getName());
				s.sendMessage("§cYou have claimed ticket " + args[0]);
				
				for (Player player: Bukkit.getOnlinePlayers()) {
					if (player.hasPermission("EtriaTickets.alerts.claim")) {
						player.sendMessage("§eTicket #" + args[0] + "claimed by " + s.getName());
					}
				}
				
				if (Bukkit.getOfflinePlayer(ticket.getCreator()).isOnline())
					Bukkit.getPlayer(ticket.getCreator()).sendMessage("§eTicket claimed by " + s.getName() + "; do /check " + args[0] + " for info");
			} else if (ticket.getAssignee().equals(s.getName())) {
				TicketManager.unassignTicket(ticket);
				s.sendMessage("§cYou have unclaimed Ticket " + args[0]);
				Bukkit.broadcast("§eTicket #" + args[0] + " unclaimed by " + s.getName(), "EtriaTickets.alerts.claim");
			} else {
				s.sendMessage("§cThat ticket isn't claimed by you");
			}
		} else {
			s.sendMessage("§cTicket not found");
		}
		return true;
	}
    
}