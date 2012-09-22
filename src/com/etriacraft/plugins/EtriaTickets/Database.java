package com.etriacraft.plugins.EtriaTickets;

import java.sql.Connection;
import com.etriacraft.lib.SQLib.SQLite;

public class Database {
    
    public SQLite sql;
    public Connection conn;
    Main main;
    
    public Database(Main instance) {
        this.main = instance;
    }
    
    public void connect() {
        sql = new SQLite(Utils.log, Main.prefix, "tickets.db", main.getDataFolder().getAbsolutePath());
        conn = sql.open();
        
        checkTables();
    }
    
    private void checkTables() {
        if (!sql.tableExists("tickets")) {
            Utils.log.info(Main.prefix + "tickets table did not exists, creating...");
            String query = "CREATE TABLE `tickets` "
                    + "(`id` INTEGER PRIMARY KEY,"
                    + " `creator` VARCHAR(32) NOT NULL,"
                    + " `message` TEXT NOT NULL,"
                    + " `timestamp` INTEGER NOT NULL,"
                    + " `comment` TEXT DEFAULT(NULL),"
                    + " `user_comment` TEXT DEFAULT (NULL),"
                    + " `world` VARCHAR(32) NOT NULL,"
                    + " `x` INTEGER NOT NULL,"
                    + " `y` INTEGER NOT NULL,"
                    + " `z` INTEGER NOT NULL,"
                    + " `held` BOOL NOT NULL DEFAULT(0),"
                    + " `completed` BOOL NOT NULL DEFAULT(0),"
                    + " `assignee` VARCHAR(32) DEFAULT(NULL));";
            
            sql.modifyQuery(query);
            Utils.log.info(Main.prefix + "tickets table created!");
        }
    }
    
    public void disconnect() {
        sql.close();
    }

}
