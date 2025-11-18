package models;

public class Motorcycle {
    private String plate;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String status;
    private String category;

    public Motorcycle(String plate, String brand, String model, int year, double price, String status, String category) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.status = status;
        this.category = category;
    }

    public String getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getCategory() { return category; }

    public void setPlate(String plate) { this.plate = plate; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("Placa: %s | Marca: %s | Modelo: %s | Año: %d | Precio: $%.2f | Estado: %s | Categoría: %s",
                plate, brand, model, year, price, status, category);
    }
}
