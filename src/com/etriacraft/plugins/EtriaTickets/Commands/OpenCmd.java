package com.etriacraft.plugins.EtriaTickets.Commands;

import com.etriacraft.plugins.EtriaTickets.TicketManager;
import com.etriacraft.plugins.EtriaTickets.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!(s instanceof Player)) return false;
        if (!Utils.hasPerm(s, "EtriaTickets.open")) {
            Utils.noPermMessage(s);
            return true;
        }
        
        if(args.length < 3) {
            s.sendMessage("§cYour message must be at least 3 words long");
            return true;
        }
        
        String message = Utils.buildString(args, 0);
        int i = TicketManager.createTicket((Player) s, message);
        s.sendMessage("§cYour Ticket ID is " + i + "; do /check " + i + " to get info on it");
        for(Player player: Bukkit.getOnlinePlayers()) {
        	if ((player.hasPermission("EtriaTickets.alerts.open"))) {
        		player.sendMessage("§eA new ticket has been filed, do /check " + i + " for more info");
        	}
        }
        return true;
    }

}