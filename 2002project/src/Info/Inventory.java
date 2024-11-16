package Info;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Inventory {
    private String medicineName;
    private int stockLevel;
    private int lowStockAlertLevel;
    private static final String medicineFilePath = "Medicine_List.xlsx";  // Path to the Excel file

    // Constructor
    public void Medicine(String medicineName, String dosage, int stockLevel, int lowStockAlertLevel) {
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


}

