package com.aplicatie.user.models;

import com.aplicatie.user.misc_objects.Constants;

public class Ticket {
    private double cost;
    private int no_tickets;
    private boolean subscription;

    public Ticket(int no_tickets) {
        this.no_tickets = no_tickets;
        this.cost = Constants.ticket_cost * this.no_tickets;
    }

    public Ticket(boolean subscription) {
        this.subscription = subscription;
        this.cost = 15;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getNo_tickets() {
        return no_tickets;
    }

    public void setNo_tickets(int no_tickets) {
        this.no_tickets = no_tickets;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        if (this.subscription)
            return "Cost: " + this.cost + " " + "Subscription: " + this.subscription;
        else
            return "Cost: " + this.cost + " " + "Tickets: " + this.no_tickets;
    }
}
