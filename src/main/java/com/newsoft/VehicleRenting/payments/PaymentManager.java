package main.java.com.newsoft.VehicleRenting.payments;
import java.nio.file.*;
import java.io.*;
import java.util.*;
public class PaymentManager {
    private final Path csvPath = Paths.get("Data", "payments.csv");
    public boolean acceptPayment(Payment p) {
        boolean ok = p.process(); // polymorphism method call 
        if (ok) {
            try {
                if (csvPath.getParent() != null) {
                    Files.createDirectories(csvPath.getParent());
                }
                boolean isNew = Files.notExists(csvPath) || Files.size(csvPath) == 0;
                try (BufferedWriter writer = Files.newBufferedWriter(csvPath, 
                        StandardOpenOption.CREATE, 
                        StandardOpenOption.APPEND)) {
                    if (isNew) {
                        writer.write("id,payerName,amount,vehicleReg,nic,paymentMethod,timestamp");
                        writer.newLine();
                    }
                    writer.write(p.toCsvRow());
                    writer.newLine();
                    writer.flush();
                    return true;
                }
            } catch (IOException e) {
                System.err.println("Failed to write payment to file: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    public List<PaymentRecord> loadAllPayments() {
        List<PaymentRecord> payments = new ArrayList<>();
        if (Files.notExists(csvPath)) {
            return payments;
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    PaymentRecord record = parseCsvRow(line);
                    payments.add(record);
                } catch (Exception e) {
                    System.err.println("Skipping malformed payment record: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read payments from file: " + e.getMessage());
            return new ArrayList<>();
        }
        return payments;
    }
    private PaymentRecord parseCsvRow(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString());
        if (fields.size() != 6) {
            throw new IllegalArgumentException("Expected 6 fields, found " + fields.size());
        }
        String id = fields.get(0);
        String payerName = fields.get(1);
        double amount = Double.parseDouble(fields.get(2));
        String method = fields.get(3);
        String details = fields.get(4);
        String timestamp = fields.get(5);
        return new PaymentRecord(id, payerName, amount, method, details, timestamp);
    }
    public Path getCsvPath() {
        return csvPath;
    }
}