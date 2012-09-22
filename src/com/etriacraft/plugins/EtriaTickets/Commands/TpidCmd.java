package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpidCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!Utils.hasPerm(s, "EtriaTickets.tpid")) {
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
            ((Player) s).teleport(ticket.getLocation());
            s.sendMessage("§cSent to the location of ticket " + args[0]);
        } else {
            s.sendMessage("§cTicket not found");
        }
        
        return true;
    }

}