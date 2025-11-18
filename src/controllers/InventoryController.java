package controllers;

import models.Motorcycle;
import structures.BinarySearchTree;
import structures.LinkedList;

public class InventoryController {
    private BinarySearchTree inventory;
    private static int motorcycleCount = 0;

    public InventoryController() {
        this.inventory = new BinarySearchTree();
    }

    public boolean registerMotorcycle(String plate, String brand, String model, int year, 
                                     double price, String status, String category) {
        if (inventory.search(plate) != null) {
            return false;
        }

        Motorcycle motorcycle = new Motorcycle(plate, brand, model, year, price, status, category);
        inventory.insert(motorcycle);
        motorcycleCount++;
        return true;
    }

    public Motorcycle searchByPlate(String plate) {
        return inventory.search(plate);
    }

    public LinkedList<Motorcycle> searchByCriteria(String field, String value) {
        return inventory.searchByCriteria(field, value);
    }

    public boolean updateMotorcycle(String plate, String brand, String model, int year, 
                                   double price, String status, String category) {
        Motorcycle motorcycle = inventory.search(plate);
        if (motorcycle == null) {
            return false;
        }

        motorcycle.setBrand(brand);
        motorcycle.setModel(model);
        motorcycle.setYear(year);
        motorcycle.setPrice(price);
        motorcycle.setStatus(status);
        motorcycle.setCategory(category);
        return true;
    }

    public boolean deleteMotorcycle(String plate) {
        Motorcycle motorcycle = inventory.search(plate);
        if (motorcycle == null || !motorcycle.getStatus().equals("Vendida")) {
            return false;
        }
        return inventory.delete(plate);
    }

    public LinkedList<Motorcycle> getAllMotorcycles() {
        return inventory.getAllMotorcycles();
    }

    public LinkedList<Motorcycle> getAvailableMotorcycles() {
        return inventory.searchByCriteria("estado", "Disponible");
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public BinarySearchTree getInventory() {
        return inventory;
    }

    public void addMotorcycle(Motorcycle motorcycle) {
        inventory.insert(motorcycle);
    }
}
