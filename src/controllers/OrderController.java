package controllers;

import models.Order;
import models.Motorcycle;
import structures.Queue;
import structures.LinkedList;

public class OrderController {
    private Queue<Order> pendingOrders;
    private static int orderCount = 0;

    public OrderController() {
        this.pendingOrders = new Queue<>();
    }

    public String addOrder(String brand, String model, int year, String customerName, String customerContact) {
        orderCount++;
        String orderId = "ORD" + String.format("%05d", orderCount);
        Order order = new Order(orderId, brand, model, year, customerName, customerContact);
        pendingOrders.enqueue(order);
        return orderId;
    }

    public Order receiveOrder(InventoryController inventoryController, String plate, double price, String category) {
        if (pendingOrders.isEmpty()) {
            return null;
        }

        Order order = pendingOrders.dequeue();
        
        // Crear motocicleta y agregarla al inventario
        Motorcycle motorcycle = new Motorcycle(
            plate,
            order.getBrand(),
            order.getModel(),
            order.getYear(),
            price,
            "Disponible",
            category
        );
        
        inventoryController.addMotorcycle(motorcycle);
        order.setStatus("Recibida");
        
        return order;
    }

    public LinkedList<Order> getPendingOrders() {
        return pendingOrders.toList();
    }

    public int getPendingOrdersCount() {
        return pendingOrders.size();
    }

    public Order peekNextOrder() {
        return pendingOrders.peek();
    }
}
