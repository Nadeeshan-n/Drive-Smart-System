package main.java.com.newsoft.VehicleRenting.payments;
public class CashPayment extends Payment {
    public CashPayment(String payerName, double amount, String vehicleReg, String nic, String phone, String address, String licenseNumber, String email, String paymentMethod) {
        super(payerName, amount, vehicleReg, nic, phone, address, licenseNumber, email, paymentMethod);
    }
    @Override
    public boolean process() { //polymorphism overriden method with logic
        return amount > 0;
    }
    public String getDetailsForCsv() {
        return "N/A";
    }
    @Override
    protected String getPaymentDetails() {
        return getDetailsForCsv();
    }
    @Override
    public String toString() {
        return String.format("CashPayment[id=%s, payer=%s, amount=%.2f, timestamp=%s]",
                getPaymentId(), getPayerName(), getAmount(), getTimestamp());
    }
}