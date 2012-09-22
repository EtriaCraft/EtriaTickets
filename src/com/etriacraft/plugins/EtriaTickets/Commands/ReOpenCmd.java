package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReOpenCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!Utils.hasPerm(s, "EtriaTickets.reopen")) {
            Utils.noPermMessage(s);
            return true;
        }
        
        if (args.length < 1) return false;
        
        if (!Utils.isInt(args[0])) {
            s.sendMessage("�cInvalid Ticket ID");
            return true;
        }
        
        Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
        
        if (ticket != null) {
            if (!ticket.isOpen()) {
                TicketManager.openTicket(ticket);
                TicketManager.unHoldTicket(ticket);
                s.sendMessage("�cYou have re-opened Ticket #" + args[0]);
                Bukkit.broadcast("�eTicket #" + args[0] + " re-opened by " + s.getName(), "EtriaTickets.alerts.reopen");
            } else {
                s.sendMessage("�cThat ticket is already open");
            }
        } else {
            s.sendMessage("�cTicket not found");
        }
        return true;
    }
}