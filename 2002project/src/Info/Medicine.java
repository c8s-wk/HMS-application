package Info;

public class Medicine {
    private String name;
    private int stockLevel;
    private int lowStockAlert;
    private boolean l;

    // constructor
    public Medicine(String name, int stockLevel, int lowStockAlert) {
        this.name = name;
        this.stockLevel = stockLevel;
        this.lowStockAlert = lowStockAlert;
    }

    // update stock level
    public void updateStockLevel(int amount) {
        this.stockLevel += amount; //amount can be negative or positive
    }

    // check if stock is low
    public boolean checkLowStock() {
        return this.stockLevel < this.lowStockAlert; //true if stock is low
    }
    //get methods
    public String getName() {
        return name;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public int getLowStockAlert() {
        return lowStockAlert;
    }


}
