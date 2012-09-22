package com.etriacraft.plugins.EtriaTickets;

import org.bukkit.Location;

public class Ticket {

    private int id;
    private String creator;
    private String message;
    private long timestamp;
    private Location loc;
    private boolean held;
    private boolean completed;
    private String assignee = null;
    private String comment = null;
    private String creatorComment = null;

    public Ticket(int id, String creator, String message, long timestamp, Location loc, boolean held, boolean completed) {
        this.id = id;
        this.creator = creator;
        this.message = message;
        this.timestamp = timestamp;
        this.loc = loc;
        this.held = held;
        this.completed = completed;
    }

    public int getId() {
        return this.id;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimeStamp() {
        return this.timestamp;
    }

    public Location getLocation() {
        return this.loc;
    }

    public boolean isHeld() {
        return this.held;
    }

    public boolean isComplete() {
        return this.completed;
    }

    public boolean isOpen() {
        return (!this.completed && !this.held);
    }

    public String getAssignee() {
        return this.assignee;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public String getCreatorComment() {
        return this.creatorComment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatorComment(String comment) {
        this.creatorComment = comment;
    }

    public void assign(String player) {
        this.assignee = player;
    }

    public void hold() {
        this.held = true;
    }

    public void unHold() {
        this.held = false;
    }

    public void complete() {
        this.completed = true;
    }

    public void unComplete() {
        this.completed = false;
    }
    
    public String getStatusString() {
        if (isOpen() && getAssignee() != null) return "§eCLAIMED";
        else if (isOpen() && getAssignee() == null) return "§aOPEN";
        else if (isHeld()) return "§6HOLD";
        else if (isComplete()) return "§cCLOSED";
        else return "unknown";
    }
    
}