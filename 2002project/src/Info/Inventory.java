package Info;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private String medicineName;
    private int stockLevel;
    private int lowStockAlertLevel;
    private static final String medicineFilePath = "2002project/Medicine_List.csv";  // Path to the CSV file

    // Constructor
    public Inventory(String medicineName, int stockLevel, int lowStockAlertLevel) {
        this.medicineName = medicineName;
        this.stockLevel = stockLevel;
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    // Getter and Setter methods
    public String getMedicineName() {
        return medicineName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getLowStockAlertLevel() {
        return lowStockAlertLevel;
    }

    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    // Method to reduce the stock level after dispensing
    public void dispenseMedicine(int quantity) {
        if (stockLevel >= quantity) {
            stockLevel -= quantity;
            System.out.println("Dispensed " + quantity + " units of " + medicineName);
        } else {
            System.out.println("Insufficient stock for " + medicineName);
        }
    }

    // Method to check if stock is below alert level
    public boolean isStockLow() {
        return stockLevel <= lowStockAlertLevel;
    }

    // Display medicine details
    public void displayMedicineInfo() {
        System.out.println("Medicine Name: " + medicineName);
        System.out.println("Stock Level: " + stockLevel);
        System.out.println("Low Stock Alert Level: " + lowStockAlertLevel);
    }

    // Load inventory from CSV file
    public static List<Inventory> loadInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(medicineFilePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                inventoryList.add(new Inventory(
                        values[0],
                        Integer.parseInt(values[1]),
                        Integer.parseInt(values[2])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    // Save inventory to CSV file
    public static void saveInventory(List<Inventory> inventoryList) {
        try (FileWriter writer = new FileWriter(medicineFilePath)) {
            writer.write("Medicine Name,Initial Stock,Low Stock Level Alert\n");
            for (Inventory inventory : inventoryList) {
                writer.write(inventory.getMedicineName() + "," +
                        inventory.getStockLevel() + "," +
                        inventory.getLowStockAlertLevel() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Inventory> inventoryList = loadInventory();

        // Display all medicines
        for (Inventory inventory : inventoryList) {
            inventory.displayMedicineInfo();
        }

        // Example: Dispense medicine and update stock
        inventoryList.get(0).dispenseMedicine(10);

        // Save changes back to the CSV
        saveInventory(inventoryList);
    }
}
