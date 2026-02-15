package main.java.com.newsoft.VehicleRenting.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import main.java.com.newsoft.VehicleRenting.repository.VehicleRepository;
import main.java.com.newsoft.VehicleRenting.model.Vehicle;
public class VehicleService {
    private final VehicleRepository repository;
    private List<Vehicle> vehicles;
    public VehicleService() {
        this.repository = new VehicleRepository();
        this.vehicles = repository.loadAllVehicles();   // load vehicles when service starts
    }
    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }
    public Vehicle getVehicle(String regNumber) {
        return repository.findByRegisterNumber(vehicles, regNumber);
    }
    public boolean addPhotoToVehicle(String regNumber, String sourceImagePath) {
        Vehicle vehicle = getVehicle(regNumber);
        if (vehicle == null) {
            System.out.println("Vehicle not found!");
            return false;
        }
        File source = new File(sourceImagePath);
        if (!source.exists()) {
            System.out.println("Image file not found!");
            return false;
        }
        File destFolder = new File("data/photos");
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        String extension = getFileExtension(source.getName());
        String newFileName = regNumber + "-" + System.currentTimeMillis() + "." + extension;
        File destFile = new File(destFolder, newFileName);
        try {
            Files.copy(source.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed to copy image: " + e.getMessage());
            return false;
        }
        String relativePath = "photos/" + newFileName;
        vehicle.addPhotoPath(relativePath);
        System.out.println("Photo added successfully: " + relativePath);
        return true;
    }
    private String getFileExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot == -1) ? "" : fileName.substring(dot + 1);
    }
}