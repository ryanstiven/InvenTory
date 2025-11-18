package views;

import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import controllers.OrderController;
import controllers.InventoryController;
import models.Order;
import structures.LinkedList;
import utils.Validator;

public class OrdersView {
    private Stage stage;
    private OrderController orderController;
    private InventoryController inventoryController;
    private MainView mainView;
    private TableView<Order> table;
    private Label statsLabel;

    public OrdersView(Stage stage, OrderController orderController, 
                     InventoryController inventoryController, MainView mainView) {
        this.stage = stage;
        this.orderController = orderController;
        this.inventoryController = inventoryController;
        this.mainView = mainView;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Título
        Label titleLabel = new Label("📦 Gestión de Órdenes");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white;");

        // Tab 1: Nueva Orden
        Tab newOrderTab = new Tab("Nueva Orden");
        newOrderTab.setClosable(false);
        newOrderTab.setContent(createNewOrderPanel());

        // Tab 2: Recibir Orden
        Tab receiveOrderTab = new Tab("Recibir Orden");
        receiveOrderTab.setClosable(false);
        receiveOrderTab.setContent(createReceiveOrderPanel());

        // Tab 3: Reporte
        Tab reportTab = new Tab("Órdenes Pendientes");
        reportTab.setClosable(false);
        reportTab.setContent(createReportPanel());

        tabPane.getTabs().addAll(newOrderTab, receiveOrderTab, reportTab);

        // Botón volver
        Button backBtn = createButton("Volver al Menú", "#34495e");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> mainView.show());

        root.getChildren().addAll(titleLabel, tabPane, backBtn);

        return new Scene(root, 900, 700);
    }

    private VBox createNewOrderPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_CENTER);

        Label subtitle = new Label("Registrar Nueva Orden");
        subtitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        TextField brandField = createTextField("Honda");
        TextField modelField = createTextField("CBR 1000RR");
        TextField yearField = createTextField("2024");
        TextField customerField = createTextField("Juan Pérez");
        TextField contactField = createTextField("+34 600 123 456");

        int row = 0;
        form.add(createLabel("Marca:"), 0, row);
        form.add(brandField, 1, row++);
        form.add(createLabel("Modelo:"), 0, row);
        form.add(modelField, 1, row++);
        form.add(createLabel("Año:"), 0, row);
        form.add(yearField, 1, row++);
        form.add(createLabel("Cliente:"), 0, row);
        form.add(customerField, 1, row++);
        form.add(createLabel("Contacto:"), 0, row);
        form.add(contactField, 1, row++);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button registerBtn = createButton("Registrar Orden", "#27ae60");
        Button clearBtn = createButton("Limpiar", "#95a5a6");

        buttonBox.getChildren().addAll(registerBtn, clearBtn);

        registerBtn.setOnAction(e -> {
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String yearStr = yearField.getText().trim();
            String customer = customerField.getText().trim();
            String contact = contactField.getText().trim();

            if (!Validator.isValidString(brand) || !Validator.isValidString(model) || 
                !Validator.isValidString(customer)) {
                showAlert("Error", "Todos los campos son obligatorios");
                return;
            }

            if (!Validator.isNumeric(yearStr)) {
                showAlert("Error", "El año debe ser numérico");
                return;
            }

            int year = Integer.parseInt(yearStr);
            if (!Validator.isValidYear(year)) {
                showAlert("Error", "Año inválido");
                return;
            }

            if (!Validator.isValidContact(contact)) {
                showAlert("Error", "Contacto inválido (mínimo 7 caracteres)");
                return;
            }

            String orderId = orderController.addOrder(brand, model, year, customer, contact);
            showAlert("Éxito", "Orden registrada correctamente\nID: " + orderId);
            
            brandField.clear();
            modelField.clear();
            yearField.clear();
            customerField.clear();
            contactField.clear();
            
            refreshReport();
        });

        clearBtn.setOnAction(e -> {
            brandField.clear();
            modelField.clear();
            yearField.clear();
            customerField.clear();
            contactField.clear();
        });

        panel.getChildren().addAll(subtitle, form, buttonBox);
        return panel;
    }

    private VBox createReceiveOrderPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_CENTER);

        Label subtitle = new Label("Recibir Orden Pendiente");
        subtitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label infoLabel = new Label("Siguiente orden en la cola:");
        infoLabel.setFont(Font.font("System", 14));

        TextArea orderInfo = new TextArea();
        orderInfo.setEditable(false);
        orderInfo.setPrefHeight(100);
        orderInfo.setStyle("-fx-font-family: monospace;");

        Button refreshBtn = createButton("Actualizar", "#3498db");
        refreshBtn.setOnAction(e -> {
            Order nextOrder = orderController.peekNextOrder();
            if (nextOrder != null) {
                orderInfo.setText(nextOrder.toString());
            } else {
                orderInfo.setText("No hay órdenes pendientes");
            }
        });

        // Formulario para recibir
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        TextField plateField = createTextField("ABC-1234");
        TextField priceField = createTextField("18000.00");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Deportiva", "Touring", "Cruiser", "Naked", "Enduro", "Scooter");
        categoryCombo.setValue("Deportiva");
        categoryCombo.setPrefWidth(250);

        int row = 0;
        form.add(createLabel("Placa:"), 0, row);
        form.add(plateField, 1, row++);
        form.add(createLabel("Precio ($):"), 0, row);
        form.add(priceField, 1, row++);
        form.add(createLabel("Categoría:"), 0, row);
        form.add(categoryCombo, 1, row++);

        Button receiveBtn = createButton("Recibir Orden", "#27ae60");
        receiveBtn.setPrefWidth(200);

        receiveBtn.setOnAction(e -> {
            if (orderController.getPendingOrdersCount() == 0) {
                showAlert("Error", "No hay órdenes pendientes para recibir");
                return;
            }

            String plate = plateField.getText().toUpperCase().trim();
            String priceStr = priceField.getText().trim();

            if (!Validator.isValidPlate(plate)) {
                showAlert("Error", "Placa inválida");
                return;
            }

            if (inventoryController.searchByPlate(plate) != null) {
                showAlert("Error", "Ya existe una motocicleta con esa placa");
                return;
            }

            if (!Validator.isNumeric(priceStr)) {
                showAlert("Error", "El precio debe ser numérico");
                return;
            }

            double price = Double.parseDouble(priceStr);
            if (!Validator.isValidPrice(price)) {
                showAlert("Error", "El precio debe ser mayor a 0");
                return;
            }

            Order received = orderController.receiveOrder(inventoryController, plate, price, categoryCombo.getValue());
            
            if (received != null) {
                showAlert("Éxito", "Orden recibida y agregada al inventario\n" + 
                         "ID: " + received.getOrderId() + "\nPlaca: " + plate);
                plateField.clear();
                priceField.clear();
                orderInfo.clear();
                refreshReport();
            } else {
                showAlert("Error", "No se pudo procesar la orden");
            }
        });

        // Cargar orden inicial
        Order nextOrder = orderController.peekNextOrder();
        if (nextOrder != null) {
            orderInfo.setText(nextOrder.toString());
        } else {
            orderInfo.setText("No hay órdenes pendientes");
        }

        panel.getChildren().addAll(subtitle, infoLabel, orderInfo, refreshBtn, 
                                   new Separator(), form, receiveBtn);
        return panel;
    }

    private VBox createReportPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        statsLabel = new Label("Total de órdenes pendientes: 0");
        statsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        table = new TableView<>();
        table.setPrefHeight(450);

        TableColumn<Order, String> idCol = new TableColumn<>("ID Orden");
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        idCol.setPrefWidth(100);

        TableColumn<Order, String> brandCol = new TableColumn<>("Marca");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(120);

        TableColumn<Order, String> modelCol = new TableColumn<>("Modelo");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(150);

        TableColumn<Order, Integer> yearCol = new TableColumn<>("Año");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Order, String> customerCol = new TableColumn<>("Cliente");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(150);

        TableColumn<Order, String> contactCol = new TableColumn<>("Contacto");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("customerContact"));
        contactCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, brandCol, modelCol, yearCol, customerCol, contactCol);

        Button refreshBtn = createButton("Actualizar", "#3498db");
        refreshBtn.setOnAction(e -> refreshReport());

        refreshReport();

        panel.getChildren().addAll(statsLabel, table, refreshBtn);
        return panel;
    }

    private void refreshReport() {
        if (table != null && statsLabel != null) {
            table.getItems().clear();
            LinkedList<Order> orders = orderController.getPendingOrders();
            
            for (int i = 0; i < orders.size(); i++) {
                table.getItems().add(orders.get(i));
            }
            
            statsLabel.setText("Total de órdenes pendientes: " + orders.size());
        }
    }

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(250);
        return field;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        return label;
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setPrefHeight(35);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                       "-fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        return button;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
