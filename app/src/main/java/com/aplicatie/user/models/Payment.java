package com.aplicatie.user.models;

public class Payment {
    private String request_response;
    private Ticket ticket;
    private String time_stamp;
    private double cost;
//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String id;
    private CreditCard creditCard;

    public Payment(String request_response, Ticket p) {
        this.request_response = request_response;
        this.ticket = p;
//        this.time_stamp = sdf.format(new Timestamp(System.currentTimeMillis()));
        this.time_stamp = request_response.split("createdAt=")[1].split(",")[1];
        id = request_response.split("id=")[1].split(",")[0];
        cost = Double.parseDouble(request_response.split("amount=")[1].split(",")[0]);
        if (request_response.contains("CreditCardDetails")) {
            creditCard = new CreditCard(
                    request_response.split("cardType=")[1].split(",")[0],
                    request_response.split("maskedNumber=")[1].split(",")[0].split("]")[0],
                    request_response.split("expirationMonth=")[1].split(",")[0],
                    request_response.split("expirationYear=")[1].split(",")[0],
                    request_response.split("cardHolderName=")[0].split(",")[0],
                    request_response.split("imageUrl=")[1].split(",")[0]
            );
        }
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket p) {
        this.ticket = p;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\n" + "Cost: " + this.cost + "\n" + "Ticket: [" + this.ticket + "]" + "\n" + "Timestamp: " + this.time_stamp + "\n" + "Card: [" + this.creditCard + "]" + "\n";
    }
}
