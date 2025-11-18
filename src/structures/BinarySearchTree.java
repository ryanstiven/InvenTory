package structures;

import models.Motorcycle;
import structures.LinkedList;

public class BinarySearchTree {
    private TreeNode root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(Motorcycle motorcycle) {
        root = insertRecursive(root, motorcycle);
    }

    private TreeNode insertRecursive(TreeNode node, Motorcycle motorcycle) {
        if (node == null) {
            return new TreeNode(motorcycle);
        }

        int comparison = motorcycle.getPlate().compareToIgnoreCase(node.data.getPlate());
        
        if (comparison < 0) {
            node.left = insertRecursive(node.left, motorcycle);
        } else if (comparison > 0) {
            node.right = insertRecursive(node.right, motorcycle);
        }
        
        return node;
    }

    public Motorcycle search(String plate) {
        return searchRecursive(root, plate);
    }

    private Motorcycle searchRecursive(TreeNode node, String plate) {
        if (node == null) {
            return null;
        }

        int comparison = plate.compareToIgnoreCase(node.data.getPlate());

        if (comparison == 0) {
            return node.data;
        } else if (comparison < 0) {
            return searchRecursive(node.left, plate);
        } else {
            return searchRecursive(node.right, plate);
        }
    }

    public boolean delete(String plate) {
        int initialSize = size();
        root = deleteRecursive(root, plate);
        return size() < initialSize;
    }

    private TreeNode deleteRecursive(TreeNode node, String plate) {
        if (node == null) {
            return null;
        }

        int comparison = plate.compareToIgnoreCase(node.data.getPlate());

        if (comparison < 0) {
            node.left = deleteRecursive(node.left, plate);
        } else if (comparison > 0) {
            node.right = deleteRecursive(node.right, plate);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            node.data = findMin(node.right);
            node.right = deleteRecursive(node.right, node.data.getPlate());
        }

        return node;
    }

    private Motorcycle findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    public LinkedList<Motorcycle> getAllMotorcycles() {
        LinkedList<Motorcycle> list = new LinkedList<>();
        inorderTraversal(root, list);
        return list;
    }

    private void inorderTraversal(TreeNode node, LinkedList<Motorcycle> list) {
        if (node != null) {
            inorderTraversal(node.left, list);
            list.add(node.data);
            inorderTraversal(node.right, list);
        }
    }

    public LinkedList<Motorcycle> searchByCriteria(String field, String value) {
        LinkedList<Motorcycle> results = new LinkedList<>();
        searchByCriteriaRecursive(root, field, value, results);
        return results;
    }

    private void searchByCriteriaRecursive(TreeNode node, String field, String value, LinkedList<Motorcycle> results) {
        if (node == null) return;

        searchByCriteriaRecursive(node.left, field, value, results);

        boolean match = false;
        switch (field.toLowerCase()) {
            case "marca":
                match = node.data.getBrand().toLowerCase().contains(value.toLowerCase());
                break;
            case "modelo":
                match = node.data.getModel().toLowerCase().contains(value.toLowerCase());
                break;
            case "año":
                match = String.valueOf(node.data.getYear()).equals(value);
                break;
            case "precio":
                try {
                    match = node.data.getPrice() <= Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    match = false;
                }
                break;
            case "estado":
                match = node.data.getStatus().equalsIgnoreCase(value);
                break;
            case "categoria":
                match = node.data.getCategory().toLowerCase().contains(value.toLowerCase());
                break;
        }

        if (match) {
            results.add(node.data);
        }

        searchByCriteriaRecursive(node.right, field, value, results);
    }

    public int size() {
        return sizeRecursive(root);
    }

    private int sizeRecursive(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRecursive(node.left) + sizeRecursive(node.right);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
