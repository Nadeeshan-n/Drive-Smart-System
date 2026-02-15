package main.java.com.newsoft.VehicleRenting.payments;
import java.time.LocalDateTime;
import java.nio.file.*;
import java.io.*;
public abstract class Payment {     //abstract class  
    private static int nextIdNumber = 1;
    private static boolean initialized = false;
    protected String paymentId;
    protected String payerName;
    protected double amount;
    protected String vehicleReg;
    protected String nic;
    protected String phone;
    protected String address;
    protected String licenseNumber;
    protected String email;
    protected String paymentMethod;
    protected LocalDateTime timestamp;
    public Payment(String payerName, double amount, String vehicleReg, String nic, String phone, String address, String licenseNumber, String email, String paymentMethod) {
        if (payerName == null || payerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Payer name cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (!initialized) {
            initializeIdCounter();
            initialized = true;
        }
        this.paymentId = generatePaymentId();
        this.payerName = payerName;
        this.amount = amount;
        this.vehicleReg = vehicleReg != null ? vehicleReg : "";
        this.nic = nic != null ? nic : "";
        this.phone = phone != null ? phone : "";
        this.address = address != null ? address : "";
        this.licenseNumber = licenseNumber != null ? licenseNumber : "";
        this.email = email != null ? email : "";
        this.paymentMethod = paymentMethod != null ? paymentMethod : "";
        this.timestamp = LocalDateTime.now();
    }
    private static synchronized void initializeIdCounter() {
        Path csvPath = Paths.get("Data", "payments.csv");
        if (Files.exists(csvPath)) {
            try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
                String line;
                String lastId = null;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty() && !line.startsWith("id,")) {
                        int commaIndex = line.indexOf(',');
                        if (commaIndex > 0) {
                            lastId = line.substring(0, commaIndex);
                        }
                    }
                }
                if (lastId != null && lastId.startsWith("PAY-")) {
                    try {
                        String numberPart = lastId.substring(4);
                        int lastNumber = Integer.parseInt(numberPart);
                        nextIdNumber = lastNumber + 1;
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
        }
    }
    private static synchronized String generatePaymentId() {
        return String.format("PAY-%05d", nextIdNumber++);
    }
    public String getPaymentId() {
        return paymentId;
    }
    public String getPayerName() {
        return payerName;
    }
    public double getAmount() {
        return amount;
    }
    public String getVehicleReg() {
        return vehicleReg;
    }
    public String getNic() {
        return nic;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public String getLicenseNumber() {
        return licenseNumber;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public abstract boolean process(); //abstract method also polymorphism common method | no  logic
    public String toCsvRow() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%.2f,%s,%s",
                paymentId,
                escapeCsv(payerName),
                escapeCsv(nic),
                escapeCsv(phone),
                escapeCsv(address),
                escapeCsv(licenseNumber),
                escapeCsv(email),
                amount,
                escapeCsv(paymentMethod),
                timestamp.toString());
    }
    protected String getPaymentDetails() {
        return "";
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
        return String.format("Payment[id=%s, payer=%s, amount=%.2f, method=%s, timestamp=%s]",
                paymentId, payerName, amount, paymentMethod, timestamp);
    }
}