package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {
    private String saleId;
    private Motorcycle motorcycle;
    private String customerName;
    private String customerContact;
    private LocalDateTime saleDate;
    private double totalAmount;

    public Sale(String saleId, Motorcycle motorcycle, String customerName, String customerContact, double totalAmount) {
        this.saleId = saleId;
        this.motorcycle = motorcycle;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.saleDate = LocalDateTime.now();
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getSaleId() { return saleId; }
    public Motorcycle getMotorcycle() { return motorcycle; }
    public String getCustomerName() { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("ID Venta: %s | Placa: %s | Marca: %s | Cliente: %s | Monto: $%.2f | Fecha: %s",
                saleId, motorcycle.getPlate(), motorcycle.getBrand(), customerName, totalAmount, saleDate.format(formatter));
    }
}
