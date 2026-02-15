package main.java.com.newsoft.VehicleRenting.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class Vehicle implements Serializable {  // main class
    private static final long serialVersionUID = 1L;
    private String registerNumber;
    private String color;
    private String description;
    private final List<String> photoPaths = new ArrayList<>();
    public Vehicle(String registerNumber, String color) {
        this.registerNumber = requireNonBlank(registerNumber, "registerNumber");
        this.color = requireNonBlank(color, "color");
    }
    private static String requireNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be null or blank");
        }
        return value;
    }
    public String getRegisterNumber() {
        return registerNumber;
    }
    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = requireNonBlank(registerNumber, "registerNumber");
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = requireNonBlank(color, "color");
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void addPhotoPath(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;
        this.photoPaths.add(relativePath);
    }
    public boolean removePhotoPath(String relativePath) {
        return this.photoPaths.remove(relativePath);
    }
    public List<String> getPhotoPaths() {
        return Collections.unmodifiableList(photoPaths);
    }
    @Override
    public String toString() {
        return String.format("Vehicle[reg=%s, color=%s, photos=%d]", registerNumber, color, photoPaths.size());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(registerNumber, vehicle.registerNumber);
    }
    @Override
    public int hashCode() {
        return Objects.hash(registerNumber);
    }
    public void displayInfo() {
        System.out.println(this.toString());
        if (!photoPaths.isEmpty()) {
            System.out.println("Photos:");
            for (String p : photoPaths) {
                System.out.println("  - " + p);
            }
        } else {
            System.out.println("No photos available.");
        }
    }
}