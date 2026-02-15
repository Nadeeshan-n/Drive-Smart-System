package main.java.com.newsoft.VehicleRenting.payments;
public class CardPayment extends Payment {
    private String maskedCardNumber;
    private String cardHolder;
    private String expiry;
    public CardPayment(String payerName, double amount, String vehicleReg, String nic, String phone, String address, String licenseNumber, String email, String paymentMethod, String cardHolder, String cardNumber, String expiry) {
        super(payerName, amount, vehicleReg, nic, phone, address, licenseNumber, email, paymentMethod);
        if (cardHolder == null || cardHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name cannot be null or empty");
        }
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        if (expiry == null || expiry.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiry date cannot be null or empty");
        }
        this.cardHolder = cardHolder.trim();
        this.maskedCardNumber = maskCardNumber(cardNumber);
        this.expiry = expiry.trim();
    }
    private String maskCardNumber(String cardNumber) {
        String digitsOnly = cardNumber.replaceAll("\\D", "");
        if (digitsOnly.length() < 4) {
            return "*".repeat(digitsOnly.length());
        }
        String lastFour = digitsOnly.substring(digitsOnly.length() - 4);
        String masked = "*".repeat(digitsOnly.length() - 4) + lastFour;
        return masked;
    }
    @Override
    public boolean process() { //polymorphism overriden method with logic
        int totalDigits = maskedCardNumber.replaceAll("\\D", "").length();
        if (totalDigits < 12 || expiry.isEmpty()) {
            return false;
        }
        try {
            Thread.sleep(80);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }
    public String getDetailsForCsv() {
        return maskedCardNumber + ";" + cardHolder;
    }
    @Override
    protected String getPaymentDetails() {
        return getDetailsForCsv();
    }
    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public String getExpiry() {
        return expiry;
    }
    @Override
    public String toString() {
        return String.format("CardPayment[id=%s, payer=%s, amount=%.2f, cardHolder=%s, card=%s, expiry=%s, timestamp=%s]",
                getPaymentId(), getPayerName(), getAmount(), cardHolder, maskedCardNumber, expiry, getTimestamp());
    }
}