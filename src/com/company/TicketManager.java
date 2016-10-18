package com.company;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.company.Ticket.staticTicketIDCounter;

public class TicketManager {

    // scanner for use with all methods
    private static Scanner scanner;

    public static void main(String[] args) throws Exception {

        // Intro stuff - scanner, bufferedWriter, ticketQueue and resolvedTickets linkedLists
        scanner = new Scanner(System.in);
        BufferedWriter openWriter = new BufferedWriter(new FileWriter("open_tickets.txt", true));
        LinkedList<Ticket> ticketQueue = new LinkedList<>();
        LinkedList<Ticket> resolvedTickets = new LinkedList<>();

        // This block adds tickets from open_tickets.txt to ticketQueue
        Scanner inputFile = new Scanner(new File("open_tickets.txt"));
        // Must use SimpleDateFormat to read the Date strings to Date objects
        String dateFormat = "EEE MMM dd hh:mm:ss z yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        while (inputFile.hasNext()) {
            Ticket openTicket = new Ticket(inputFile.nextLine(), Integer.parseInt(inputFile.nextLine()), inputFile.nextLine(),
                    formatter.parse(inputFile.nextLine()), null, null);
            ticketQueue.add(openTicket);
        }

        while (true) {

            System.out.println("1. Enter Ticket\n2. Delete by ID\n3. Delete by Issue\n" +
                    "4. Search by Name\n5. Print all tickets\n6. Quit");
            int task = getPositiveIntInput();

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete a ticket
                deleteTicket(ticketQueue, resolvedTickets);
                System.out.println(resolvedTickets);

            } else if (task == 3) {
                searchTicketListDescription(ticketQueue);
                deleteTicket(ticketQueue, resolvedTickets);

            } else if (task == 4) {
                searchTicketListDescription(ticketQueue);

            } else if (task == 6) {
                // This lets me overwrite what's already in the file so there aren't repeats
                BufferedWriter writer = new BufferedWriter(new FileWriter("open_tickets.txt"));
                // Adding ticket information to file
                for (Ticket t : ticketQueue) {
                    writer.write(t.getDescription() + "\r\n" + t.getPriority() + "\r\n" +
                                     t.getReporter() + "\r\n" + t.getDateReported() + "\r\n");
                }
                // Creating file with resolved tickets
                String date = new SimpleDateFormat("MMM_dd_yyyy").format(new Date());
                String closedTickets = "Resolved_tickets_as_of_" + date + ".txt";
                BufferedWriter closedWriter = new BufferedWriter(new FileWriter(closedTickets));
                for (Ticket t : resolvedTickets) {
                    closedWriter.write("Problem: " + t.getDescription() + "\r\nPriority: " + t.getPriority() +
                            "\r\nReporter: " + t.getReporter() + "\r\nDate reported: " + t.getDateReported() +
                            "\r\nResolved Date: " + t.getResolvedDate() + "\r\nResolution: " + t.getResolution() + "\r\n");
                }
                closedWriter.close();
                openWriter.close();
                writer.close();
                System.out.println("Quitting program");
                break;

            } else {
                //this will happen for 5 or any other selection that is a valid int
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }
    }

    private static void deleteTicket(LinkedList<Ticket> ticketQueue, LinkedList<Ticket> resolvedTickets) {
        printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        System.out.println("Enter ID of ticket to delete");
        int deleteID = getPositiveIntInput();
        System.out.println("What was the resolution?");
        String resolution = scanner.nextLine();
        Date today = new Date();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticket.setResolution(resolution);
                ticket.setDate(today);
                resolvedTickets.add(ticket);
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }
        if (!found) {
            System.out.println("Ticket ID not found, no ticket deleted. Please try again.\nReturning to menu...");
        }
        printAllTickets(ticketQueue);  //print updated list
    }

    private static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates Date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description + " on a scale of 1 - 5");
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported, null, null);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add? Enter 'n' for no or anything else for yes.");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    private static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }
        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end
        int newTicketPriority = newTicket.getPriority();
        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking
            // at if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }
        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }

    private static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");
    }

    // Error checking each time a user is asked to enter a positive integer
    private static int getPositiveIntInput() {
        while (true) {
            try {
                String stringInput = scanner.nextLine();
                int intInput = Integer.parseInt(stringInput);
                if (intInput >= 0) {
                    return intInput;
                } else {
                    System.out.println("Please enter a positive number");
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please enter a positive number");
            }
        }
    }

    // Asks the user for a String and searches for that String in all ticket Descriptions
    // Returns a list of tickets
    private static void searchTicketListDescription(LinkedList<Ticket> ticketQueue) {
        ArrayList<Ticket> ticketSearchResults = new ArrayList<>();
        System.out.println("What would you like to search?");
        String searchString = scanner.nextLine();
        for (Ticket t : ticketQueue) {
            if (t.getDescription().contains(searchString)) {
                ticketSearchResults.add(t);
            }
        }
        System.out.println("Here are the tickets found:\n" + ticketSearchResults);
    }


}