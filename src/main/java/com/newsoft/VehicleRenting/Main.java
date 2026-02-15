package main.java.com.newsoft.VehicleRenting;
import javax.swing.SwingUtilities;
import main.java.com.newsoft.VehicleRenting.ui.VehicleRentalUI;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VehicleRentalUI ui = new VehicleRentalUI();
            ui.setVisible(true);
        });
    }
}