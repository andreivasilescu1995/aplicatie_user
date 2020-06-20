package com.aplicatie.user.models;

public class CreditCard {
    private String type;
    private String maskedNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cardHolderName;
    private String imageUrl;

    public CreditCard(String type, String maskedNumber, String expirationMonth, String expirationYear, String cardHolderName, String imageUrl) {
        this.type = type;
        this.maskedNumber = maskedNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cardHolderName = cardHolderName;
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public void setMaskedNumber(String last4) {
        this.maskedNumber = last4;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Type: " + this.type + ", MaskedNumber: " + this.maskedNumber + ", Expiration: " + this.expirationMonth + "/" + this.expirationYear + ", Card holder: " + this.cardHolderName + ", imageUrl: " + this.imageUrl;
    }
}
