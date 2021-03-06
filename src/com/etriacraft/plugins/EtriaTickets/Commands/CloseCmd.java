package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloseCmd implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (!Utils.hasPerm(s, "EtriaTickets.close")) {
			Utils.noPermMessage(s);
			return true;
		}
		
		if (args.length < 1) return false;
		
		if (!Utils.isInt(args[0])) {
			s.sendMessage("Invalid Ticket ID");
			return true;
		}
		
		Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
		
		if (ticket != null) {
			if (!ticket.isComplete()) {
				if (ticket.getAssignee() == null) {
					s.sendMessage("�cThis ticket must be claimed before it can be closed");
					return true;
				}
				ticket.complete();
				TicketManager.closeTicket(ticket);
				TicketManager.unHoldTicket(ticket);
				s.sendMessage("�cYou have closed Ticket �3" + args[0]);
				
				for (Player player: Bukkit.getOnlinePlayers()) {
					if ((player.hasPermission("EtriaTickets.alerts.close"))) {
						player.sendMessage("�eTicket # �3" + args[0] + "�e closed by �3" + s.getName());
				}
			}				
				if (Bukkit.getOfflinePlayer(ticket.getCreator()).isOnline())
					Bukkit.getPlayer(ticket.getCreator()).sendMessage("�eTicket closed by �3" + s.getName() + "�e; do /check �3" + args[0] + "�e for info");
			} else {
				s.sendMessage("�cThat ticket isn't open");
			}
		} else {
			s.sendMessage("�cTicket not found");
		}
		
		return true;
	}
	
}