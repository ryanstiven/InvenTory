package views;

import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import controllers.*;

public class MainView {
    private Stage stage;
    private InventoryController inventoryController;
    private OrderController orderController;
    private SaleController saleController;

    public MainView(Stage stage, InventoryController inventoryController, 
                   OrderController orderController, SaleController saleController) {
        this.stage = stage;
        this.inventoryController = inventoryController;
        this.orderController = orderController;
        this.saleController = saleController;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("INVENTORY - Sistema de Gestión");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label statsLabel = new Label(
            String.format("Inventario: %d | Órdenes: %d | Ventas: %d",
                inventoryController.getInventorySize(),
                orderController.getPendingOrdersCount(),
                saleController.getTotalSales())
        );
        statsLabel.setFont(Font.font("System", 14));
        statsLabel.setStyle("-fx-text-fill: #7f8c8d;");

        Button registerBtn = createMenuButton("Registrar Motocicleta");
        Button searchBtn = createMenuButton("Buscar Motocicleta");
        Button updateBtn = createMenuButton("Actualizar Motocicleta");
        Button ordersBtn = createMenuButton("Gestionar Órdenes");
        Button salesBtn = createMenuButton("Gestionar Ventas");
        Button exitBtn = createMenuButton("Salir");
        exitBtn.setStyle(exitBtn.getStyle() + "-fx-background-color: #e74c3c;");

        registerBtn.setOnAction(e -> showRegisterView());
        searchBtn.setOnAction(e -> showSearchView());
        updateBtn.setOnAction(e -> showUpdateView());
        ordersBtn.setOnAction(e -> showOrdersView());
        salesBtn.setOnAction(e -> showSalesView());
        exitBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(titleLabel, statsLabel, registerBtn, searchBtn, 
                                  updateBtn, ordersBtn, salesBtn, exitBtn);

        return new Scene(root, 600, 700);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(350);
        button.setPrefHeight(50);
        button.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                       "-fx-background-radius: 8; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #2980b9; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #3498db; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand;"));
        return button;
    }

    private void showRegisterView() {
        RegisterView registerView = new RegisterView(stage, inventoryController, this);
        stage.setScene(registerView.createScene());
    }

    private void showSearchView() {
        SearchView searchView = new SearchView(stage, inventoryController, this);
        stage.setScene(searchView.createScene());
    }

    private void showUpdateView() {
        UpdateView updateView = new UpdateView(stage, inventoryController, this);
        stage.setScene(updateView.createScene());
    }

    private void showOrdersView() {
        OrdersView ordersView = new OrdersView(stage, orderController, inventoryController, this);
        stage.setScene(ordersView.createScene());
    }

    private void showSalesView() {
        SalesView salesView = new SalesView(stage, saleController, inventoryController, this);
        stage.setScene(salesView.createScene());
    }

    public void show() {
        stage.setScene(createScene());
    }
}
