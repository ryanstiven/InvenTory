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
import controllers.SaleController;
import controllers.InventoryController;
import models.Sale;
import models.Motorcycle;
import structures.LinkedList;
import utils.Validator;
import utils.Formatter;

public class SalesView {
    private Stage stage;
    private SaleController saleController;
    private InventoryController inventoryController;
    private MainView mainView;
    private TableView<Sale> table;
    private Label statsLabel;
    private Motorcycle selectedMotorcycle;

    public SalesView(Stage stage, SaleController saleController, 
                    InventoryController inventoryController, MainView mainView) {
        this.stage = stage;
        this.saleController = saleController;
        this.inventoryController = inventoryController;
        this.mainView = mainView;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Título
        Label titleLabel = new Label("Gestión de Ventas");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white;");

        // Tab 1: Nueva Venta
        Tab newSaleTab = new Tab("Nueva Venta");
        newSaleTab.setClosable(false);
        newSaleTab.setContent(createNewSalePanel());

        // Tab 2: Historial
        Tab historyTab = new Tab("Historial de Ventas");
        historyTab.setClosable(false);
        historyTab.setContent(createHistoryPanel());

        tabPane.getTabs().addAll(newSaleTab, historyTab);

        // Botón volver
        Button backBtn = createButton("Volver al Menú", "#34495e");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> mainView.show());

        root.getChildren().addAll(titleLabel, tabPane, backBtn);

        return new Scene(root, 950, 700);
    }

    private VBox createNewSalePanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_CENTER);

        Label subtitle = new Label("Registrar Nueva Venta");
        subtitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Buscar motocicleta
        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);

        HBox searchRow = new HBox(15);
        searchRow.setAlignment(Pos.CENTER);

        Label searchLabel = new Label("Buscar Motocicleta:");
        TextField plateField = createTextField("Placa");
        Button searchBtn = createButton("Buscar", "#3498db");

        searchRow.getChildren().addAll(searchLabel, plateField, searchBtn);

        TextArea motorcycleInfo = new TextArea();
        motorcycleInfo.setEditable(false);
        motorcycleInfo.setPrefHeight(80);
        motorcycleInfo.setStyle("-fx-font-family: monospace;");

        searchBox.getChildren().addAll(searchRow, motorcycleInfo);

        // Formulario de venta
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setVisible(false);

        TextField customerField = createTextField("Nombre del cliente");
        TextField contactField = createTextField("Teléfono/Email");
        TextField priceField = createTextField("Precio final");

        int row = 0;
        form.add(createLabel("Cliente:"), 0, row);
        form.add(customerField, 1, row++);
        form.add(createLabel("Contacto:"), 0, row);
        form.add(contactField, 1, row++);
        form.add(createLabel("Precio Final ($):"), 0, row);
        form.add(priceField, 1, row++);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button sellBtn = createButton("Registrar Venta", "#27ae60");
        Button cancelBtn = createButton("Cancelar", "#95a5a6");

        buttonBox.getChildren().addAll(sellBtn, cancelBtn);

        // Eventos
        searchBtn.setOnAction(e -> {
            String plate = Formatter.formatPlate(plateField.getText());
            Motorcycle motorcycle = inventoryController.searchByPlate(plate);

            if (motorcycle != null) {
                if (!motorcycle.getStatus().equals("Disponible")) {
                    showAlert("Error", "La motocicleta no está disponible para venta");
                    motorcycleInfo.clear();
                    form.setVisible(false);
                    selectedMotorcycle = null;
                    return;
                }

                selectedMotorcycle = motorcycle;
                motorcycleInfo.setText(motorcycle.toString());
                priceField.setText(String.valueOf(motorcycle.getPrice()));
                form.setVisible(true);
            } else {
                showAlert("No encontrado", "No existe motocicleta con esa placa");
                motorcycleInfo.clear();
                form.setVisible(false);
                selectedMotorcycle = null;
            }
        });

        sellBtn.setOnAction(e -> {
            if (selectedMotorcycle == null) {
                showAlert("Error", "Primero debe buscar una motocicleta");
                return;
            }

            String customer = customerField.getText().trim();
            String contact = contactField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (!Validator.isValidString(customer)) {
                showAlert("Error", "El nombre del cliente es obligatorio");
                return;
            }

            if (!Validator.isValidContact(contact)) {
                showAlert("Error", "Contacto inválido (mínimo 7 caracteres)");
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

            String saleId = saleController.registerSale(selectedMotorcycle, customer, contact, 
                                                       price, inventoryController);
            
            showAlert("Éxito", "Venta registrada correctamente\nID: " + saleId + 
                     "\nPlaca: " + selectedMotorcycle.getPlate());

            // Limpiar
            plateField.clear();
            motorcycleInfo.clear();
            customerField.clear();
            contactField.clear();
            priceField.clear();
            form.setVisible(false);
            selectedMotorcycle = null;
            
            refreshHistory();
        });

        cancelBtn.setOnAction(e -> {
            plateField.clear();
            motorcycleInfo.clear();
            customerField.clear();
            contactField.clear();
            priceField.clear();
            form.setVisible(false);
            selectedMotorcycle = null;
        });

        panel.getChildren().addAll(subtitle, searchBox, new Separator(), form, buttonBox);
        return panel;
    }

    private VBox createHistoryPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        statsLabel = new Label();
        statsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statsLabel.setStyle("-fx-text-fill: #2c3e50;");

        table = new TableView<>();
        table.setPrefHeight(450);

        TableColumn<Sale, String> idCol = new TableColumn<>("ID Venta");
        idCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        idCol.setPrefWidth(100);

        TableColumn<Sale, String> plateCol = new TableColumn<>("Placa");
        plateCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getMotorcycle().getPlate()));
        plateCol.setPrefWidth(100);

        TableColumn<Sale, String> brandCol = new TableColumn<>("Marca");
        brandCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getMotorcycle().getBrand()));
        brandCol.setPrefWidth(120);

        TableColumn<Sale, String> modelCol = new TableColumn<>("Modelo");
        modelCol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().getMotorcycle().getModel()));
        modelCol.setPrefWidth(150);

        TableColumn<Sale, String> customerCol = new TableColumn<>("Cliente");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(150);

        TableColumn<Sale, Double> amountCol = new TableColumn<>("Monto");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        amountCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, plateCol, brandCol, modelCol, customerCol, amountCol);

        Button refreshBtn = createButton("Actualizar", "#3498db");
        refreshBtn.setOnAction(e -> refreshHistory());

        refreshHistory();

        panel.getChildren().addAll(statsLabel, table, refreshBtn);
        return panel;
    }

    private void refreshHistory() {
        if (table != null && statsLabel != null) {
            table.getItems().clear();
            LinkedList<Sale> sales = saleController.getSalesHistory();
            
            for (int i = 0; i < sales.size(); i++) {
                table.getItems().add(sales.get(i));
            }
            
            double revenue = saleController.getTotalRevenue();
            statsLabel.setText(String.format("Total de ventas: %d | Ingresos totales: %s",
                                            sales.size(), Formatter.formatPrice(revenue)));
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
