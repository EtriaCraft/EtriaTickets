package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HoldCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!Utils.hasPerm(s, "EtriaTickets.hold")) {
            Utils.noPermMessage(s);
            return true;
        }
        
        if (args.length < 1) return false;
        
        if (!Utils.isInt(args[0])) {
            s.sendMessage("§cInvalid ticket ID");
            return true;
        }
        
        Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
        
        if (ticket != null) {
            if (ticket.isOpen()) {
                ticket.hold();
                TicketManager.holdTicket(ticket);
                s.sendMessage("§cTicket " + args[0] + " is now on hold");
                
                for(Player player: Bukkit.getOnlinePlayers()) {
                	if ((player.hasPermission("EtriaTickets.alerts.hold"))) {
                		player.sendMessage("§eTicket #" + args[0] + " put on hold by " + s.getName());
                	}
                }
            } else {
                s.sendMessage("§cThat ticket cannot be put on hold");
            }
        } else {
            s.sendMessage("§cTicket does not exist, try another ID?");
        }
        
        return true;
    }

}