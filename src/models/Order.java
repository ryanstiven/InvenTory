package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private String orderId;
    private String brand;
    private String model;
    private int year;
    private String customerName;
    private String customerContact;
    private LocalDateTime orderDate;
    private String status; // "Pendiente", "Recibida"

    public Order(String orderId, String brand, String model, int year, String customerName, String customerContact) {
        this.orderId = orderId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.orderDate = LocalDateTime.now();
        this.status = "Pendiente";
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getCustomerName() { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("ID: %s | Marca: %s | Modelo: %s | Año: %d | Cliente: %s | Fecha: %s | Estado: %s",
                orderId, brand, model, year, customerName, orderDate.format(formatter), status);
    }
}
