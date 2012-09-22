package com.etriacraft.plugins.EtriaTickets;

import com.etriacraft.plugins.EtriaTickets.Commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    
    public static Database db;
    public static String prefix;

    @Override
    public void onEnable() {
        prefix = "[" + getDescription().getName() +  "] ";
        
        db = new Database(this);
        db.connect();
        TicketManager.loadTickets();
        loadCmds();
    }
    
    @Override
    public void onDisable() {
        db.disconnect();
    }

    private void loadCmds() {
        getCommand("hold").setExecutor(new HoldCmd());
        getCommand("open").setExecutor(new OpenCmd());
        getCommand("check").setExecutor(new CheckCmd());
        getCommand("close").setExecutor(new CloseCmd());
        getCommand("tpid").setExecutor(new TpidCmd());
        getCommand("reopen").setExecutor(new ReOpenCmd());
        getCommand("comment").setExecutor(new CommentCmd());
        getCommand("claim").setExecutor(new ClaimCmd());
    }
    
}