package main.java.com.newsoft.VehicleRenting.payments;
public class PaymentRecord {
    private final String id;
    private final String payerName;
    private final double amount;
    private final String method;
    private final String details;
    private final String timestamp;
    public PaymentRecord(String id, String payerName, double amount, String method, String details, String timestamp) {
        this.id = id;
        this.payerName = payerName;
        this.amount = amount;
        this.method = method;
        this.details = details;
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }
    public String getPayerName() {
        return payerName;
    }
    public double getAmount() {
        return amount;
    }
    public String getMethod() {
        return method;
    }
    public String getDetails() {
        return details;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public static PaymentRecord fromPayment(Payment p) {
        String details;
        if (p instanceof CardPayment) {
            details = ((CardPayment) p).getDetailsForCsv();
        } else if (p instanceof CashPayment) {
            details = ((CashPayment) p).getDetailsForCsv();
        } else {
            details = "";
        }
        return new PaymentRecord(
            p.getPaymentId(),
            p.getPayerName(),
            p.getAmount(),
            p.getPaymentMethod(),
            details,
            p.getTimestamp().toString()
        );
    }
    public String toCsvRow() {
        return String.format("%s,%s,%.2f,%s,%s,%s",
                escapeCsv(id),
                escapeCsv(payerName),
                amount,
                escapeCsv(method),
                escapeCsv(details),
                escapeCsv(timestamp));
    }
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    @Override
    public String toString() {
        return String.format("PaymentRecord[id=%s, payer=%s, amount=%.2f, method=%s, details=%s, timestamp=%s]",
                id, payerName, amount, method, details, timestamp);
    }
}