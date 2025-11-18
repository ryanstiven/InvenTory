package controllers;

import models.Sale;
import models.Motorcycle;
import structures.Stack;
import structures.LinkedList;

public class SaleController {
    private Stack<Sale> salesHistory;
    private static int saleCount = 0;

    public SaleController() {
        this.salesHistory = new Stack<>();
    }

    public String registerSale(Motorcycle motorcycle, String customerName, String customerContact, 
                              double totalAmount, InventoryController inventoryController) {
        saleCount++;
        String saleId = "SALE" + String.format("%05d", saleCount);
        
        // Marcar motocicleta como vendida
        motorcycle.setStatus("Vendida");
        
        // Crear venta
        Sale sale = new Sale(saleId, motorcycle, customerName, customerContact, totalAmount);
        salesHistory.push(sale);
        
        // Eliminar del inventario
        inventoryController.deleteMotorcycle(motorcycle.getPlate());
        
        return saleId;
    }

    public LinkedList<Sale> getSalesHistory() {
        return salesHistory.toList();
    }

    public int getTotalSales() {
        return salesHistory.size();
    }

    public double getTotalRevenue() {
        double total = 0;
        LinkedList<Sale> sales = salesHistory.toList();
        
        for (int i = 0; i < sales.size(); i++) {
            Sale sale = sales.get(i);
            if (sale != null) {
                total += sale.getTotalAmount();
            }
        }
        
        return total;
    }

    public Sale getLastSale() {
        return salesHistory.peek();
    }
}
