package com.company;

import java.io.*;
import java.util.Date;
public class Ticket {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private Date resolvedDate;
    private String resolution;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    private static int staticTicketIDCounter = 1;

    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;

    public Ticket(String desc, int p, String rep, Date date, Date rDate, String resolution) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.resolvedDate = rDate;
        this.resolution = resolution;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    private static int readTicketIDCounter() throws Exception {
        int currentNum = 1;
        FileReader input = new FileReader("current_tickedID.txt");
        currentNum = input.read();
        input.close();
        return currentNum;
    }

    protected String getDescription() {
        return description;
    }

    protected int getPriority() {
        return priority;
    }

    protected String getReporter() {
        return reporter;
    }

    protected Date getDateReported() {
        return dateReported;
    }

    protected Date getResolvedDate() {
        return resolvedDate;
    }

    protected String getResolution() {
        return resolution;
    }

    protected int getTicketID() {
        return ticketID;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setDate(Date rDate) {
        this.resolvedDate = rDate;
    }

    public String toString(){
        return("ID: " + this.ticketID + " Issues: " + this.description + " Priority: " + this.priority +
                " Reported by: " + this.reporter + " Reported on: " + this.dateReported + " Resolution date: " +
                this.resolvedDate + " Resolution: " + this.resolution);
    }
}