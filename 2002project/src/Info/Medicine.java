package Info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Medicine {
    private String name;
    private int stock;
    private int lowStockAlertLevel;
    private boolean replenishmentRequest;

    private static final String MEDICINE_FILE_PATH = "2002project/Medicine_List.csv"; // Update with the correct CSV file path

    // Constructor
    public Medicine(String name, int stock, int lowStockAlertLevel) {
        this.name = name;
        this.stock = stock;
        this.lowStockAlertLevel = lowStockAlertLevel;
        this.replenishmentRequest = stock < lowStockAlertLevel;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }


    public int getLowStockAlertLevel() {
        return lowStockAlertLevel;
    }
    public boolean getReplenishmentRequest() {
        return replenishmentRequest;
    }

    public void setStock(int stock) {
        this.stock = stock;
        checkAndSetReplenishmentRequest();
        updateMedicineInCSV();
    }

    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        this.lowStockAlertLevel = lowStockAlertLevel;
        checkAndSetReplenishmentRequest();
        updateMedicineInCSV();
    }//should delete

    public void setReplenishmentRequest(boolean replenishmentRequest) {
        this.replenishmentRequest = replenishmentRequest;
        //updateMedicineInCSV();
    }

    // Check if stock is below lowStockAlertLevel and update replenishmentRequest
    public void checkAndSetReplenishmentRequest() {
        setReplenishmentRequest(this.stock < this.lowStockAlertLevel);
    }

    // Load medicines from CSV
    public static List<Medicine> loadMedicinesFromCSV() {
        List<Medicine> inventory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length < 3) continue;

                String medicineName = data[0].trim();
                int stock = Integer.parseInt(data[1].trim());
                int lowStockAlert = Integer.parseInt(data[2].trim());

                inventory.add(new Medicine(medicineName, stock, lowStockAlert));
            }
        } catch (IOException e) {
            System.err.println("Error reading medicine file: " + e.getMessage());
        }
        return inventory;
    }

    // Save medicines to CSV
    public static void saveMedicinesToCSV(List<Medicine> medicines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_FILE_PATH))) {
            // Write header
            bw.write("Medicine Name,Initial Stock,Low Stock Level Alert,Replenishment Request");
            bw.newLine();
            // Write data
            for (Medicine medicine : medicines) {
                bw.write(medicine.toCSV());
                bw.newLine();
            }
            System.out.println("Medicine data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error writing medicine inventory CSV: " + e.getMessage());
        }
    }

    // Update this medicine in CSV
    private void updateMedicineInCSV() {
        List<Medicine> medicines = loadMedicinesFromCSV();
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).getName().equalsIgnoreCase(this.name)) {
                medicines.set(i, this);
                break;
            }
        }
        saveMedicinesToCSV(medicines);
    }

    // Convert to CSV format
    public String toCSV() {
        return name + "," + stock + "," + lowStockAlertLevel + "," + replenishmentRequest;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "name='" + name + '\'' +
                ", stock=" + stock +
                ", lowStockAlertLevel=" + lowStockAlertLevel +
                ", replenishmentRequest=" + replenishmentRequest +
                '}';
    }
}
