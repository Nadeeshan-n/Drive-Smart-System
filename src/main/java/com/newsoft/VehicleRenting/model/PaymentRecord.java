package main.java.com.newsoft.VehicleRenting.model;
import java.util.UUID;
public abstract class PaymentRecord {
    protected String paymentId;
    protected String refId;
    protected double amount;
    protected String date;   // ISO 8601
    protected String status; // PAID / PENDING / FAILED
    protected PaymentRecord(String paymentId, String refId, double amount, String date, String status) {
        this.paymentId = paymentId;
        this.refId = refId;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }
    protected PaymentRecord(String refId, double amount, String date, String status) {
        this(UUID.randomUUID().toString(), refId, amount, date, status);
    }
    public abstract String getMethod();
    public abstract String getCardMasked();
    public String getPaymentId() { return paymentId; }
    public String getRefId() { return refId; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String toCsvRow() {
        return String.join(",",
            escape(paymentId),
            escape(refId),
            String.format(java.util.Locale.US, "%.2f", amount),
            escape(getMethod()),
            escape(getCardMasked()),
            escape(date),
            escape(status)
        );
    }
    private String escape(String s) {
        if (s == null) return "";
        return s.replaceAll("[\\r\\n]", " ").replace(",", "");
    }
}