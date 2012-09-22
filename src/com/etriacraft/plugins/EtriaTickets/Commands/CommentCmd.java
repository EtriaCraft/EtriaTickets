package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.Ticket;
import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommentCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {        
        if (args.length < 2) return false;
        
        if (!Utils.isInt(args[0])) {
            s.sendMessage("§cInvalid Ticket ID");
            return true;
        }
        
        String message = Utils.buildString(args, 1);
        
        Ticket ticket = TicketManager.getTicket(Utils.getInt(args[0]));
        
        if (ticket != null) {
            if (ticket.getAssignee() != null && ticket.getAssignee().equals(s.getName())) {
                if (!Utils.hasPerm(s, "EtriaTickets.comment.mod")) {
                    Utils.noPermMessage(s);
                } else {
                    if (ticket.getAssignee() == null) {
                        s.sendMessage("§cYou must claim this ticket before you can comment on it");
                        return true;
                    }

                    if (ticket.getComment() == null) {
                        s.sendMessage("§cYou have added a comment on Ticket " + args[0]);
                    } else {
                        s.sendMessage("§cYou have updated the comment on Ticket " + args[0]);
                    }
                    TicketManager.setTicketComment(ticket, message, true);
                    if (Bukkit.getPlayer(ticket.getCreator()) != null)
                        Bukkit.getPlayer(ticket.getCreator()).sendMessage("§c" + s.getName() + " commented on your ticket, do /check " + args[0]);
                    for(Player player: Bukkit.getOnlinePlayers()) {
                    	if ((player.hasPermission("EtriaTickets.alerts.comment"))) {
                    		player.sendMessage("§c" + s.getName() + " commented on Ticket #" + args[0]);
                    	}
                    }
                }
            } else if (ticket.getCreator().equals(s.getName())){
                if (!Utils.hasPerm(s, "EtriaTickets.comment.own")) {
                    Utils.noPermMessage(s);
                } else {
                    if (ticket.getCreatorComment() == null) {
                        s.sendMessage("§cYou have added a comment on Ticket " + args[0]);
                    } else {
                        s.sendMessage("§cYou have updated the comment on Ticket " + args[0]);
                    }
                    TicketManager.setTicketComment(ticket, message, false);
                    Bukkit.broadcast("§c" + s.getName() + " has commented on their ticket, do /check " + args[0], "EtriaTickets.alerts.comment");
                }
            } else {
                s.sendMessage("§cYou're not allowed to comment on this ticket");
            }
        } else {
            s.sendMessage("§cTicket not found");
        }
        return true;
    }

}