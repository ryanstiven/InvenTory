package views;

import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import controllers.InventoryController;
import models.Motorcycle;
import utils.Validator;
import utils.Formatter;

public class UpdateView {
    private Stage stage;
    private InventoryController inventoryController;
    private MainView mainView;
    private Motorcycle currentMotorcycle;

    public UpdateView(Stage stage, InventoryController inventoryController, MainView mainView) {
        this.stage = stage;
        this.inventoryController = inventoryController;
        this.mainView = mainView;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Título
        Label titleLabel = new Label("Actualizar Motocicleta");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Panel de búsqueda
        VBox searchPanel = new VBox(15);
        searchPanel.setPadding(new Insets(20));
        searchPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        searchPanel.setAlignment(Pos.CENTER);

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER);

        Label searchLabel = new Label("Buscar por Placa:");
        searchLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        TextField searchField = new TextField();
        searchField.setPromptText("ABC-1234");
        searchField.setPrefWidth(200);

        Button searchBtn = createButton("Buscar", "#3498db");

        searchBox.getChildren().addAll(searchLabel, searchField, searchBtn);
        searchPanel.getChildren().add(searchBox);

        // Formulario de actualización
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        form.setVisible(false);

        // Campos
        Label plateLabel = new Label();
        plateLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        plateLabel.setStyle("-fx-text-fill: #e74c3c;");

        TextField brandField = createTextField("Honda");
        TextField modelField = createTextField("CBR 600RR");
        TextField yearField = createTextField("2023");
        TextField priceField = createTextField("15000.00");
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Disponible", "Reservada", "Vendida");
        statusCombo.setPrefWidth(250);
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Deportiva", "Touring", "Cruiser", "Naked", "Enduro", "Scooter");
        categoryCombo.setPrefWidth(250);

        // Agregar al formulario
        int row = 0;
        form.add(new Label("Placa:"), 0, row);
        form.add(plateLabel, 1, row++);
        form.add(createLabel("Marca:"), 0, row);
        form.add(brandField, 1, row++);
        form.add(createLabel("Modelo:"), 0, row);
        form.add(modelField, 1, row++);
        form.add(createLabel("Año:"), 0, row);
        form.add(yearField, 1, row++);
        form.add(createLabel("Precio ($):"), 0, row);
        form.add(priceField, 1, row++);
        form.add(createLabel("Estado:"), 0, row);
        form.add(statusCombo, 1, row++);
        form.add(createLabel("Categoría:"), 0, row);
        form.add(categoryCombo, 1, row++);

        // Botones
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button updateBtn = createButton("Actualizar", "#27ae60");
        Button deleteBtn = createButton("Eliminar", "#e74c3c");
        Button cancelBtn = createButton("Cancelar", "#95a5a6");
        Button backBtn = createButton("Volver", "#34495e");

        buttonBox.getChildren().addAll(updateBtn, deleteBtn, cancelBtn, backBtn);

        // Eventos
        searchBtn.setOnAction(e -> {
            String plate = Formatter.formatPlate(searchField.getText());
            Motorcycle motorcycle = inventoryController.searchByPlate(plate);
            
            if (motorcycle != null) {
                currentMotorcycle = motorcycle;
                plateLabel.setText(motorcycle.getPlate());
                brandField.setText(motorcycle.getBrand());
                modelField.setText(motorcycle.getModel());
                yearField.setText(String.valueOf(motorcycle.getYear()));
                priceField.setText(String.valueOf(motorcycle.getPrice()));
                statusCombo.setValue(motorcycle.getStatus());
                categoryCombo.setValue(motorcycle.getCategory());
                form.setVisible(true);
            } else {
                form.setVisible(false);
                showAlert("No encontrado", "No existe motocicleta con esa placa");
            }
        });

        updateBtn.setOnAction(e -> {
            if (currentMotorcycle == null) return;

            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String yearStr = yearField.getText().trim();
            String priceStr = priceField.getText().trim();
            String status = statusCombo.getValue();
            String category = categoryCombo.getValue();

            // Validaciones
            if (!Validator.isValidString(brand) || !Validator.isValidString(model)) {
                showAlert("Error", "Marca y modelo son obligatorios");
                return;
            }
            if (!Validator.isNumeric(yearStr) || !Validator.isNumeric(priceStr)) {
                showAlert("Error", "Año y precio deben ser numéricos");
                return;
            }
            
            int year = Integer.parseInt(yearStr);
            double price = Double.parseDouble(priceStr);
            
            if (!Validator.isValidYear(year) || !Validator.isValidPrice(price)) {
                showAlert("Error", "Año o precio inválidos");
                return;
            }

            boolean success = inventoryController.updateMotorcycle(
                currentMotorcycle.getPlate(), brand, model, year, price, status, category
            );

            if (success) {
                showAlert("Éxito", "Motocicleta actualizada correctamente");
                form.setVisible(false);
                searchField.clear();
                currentMotorcycle = null;
            } else {
                showAlert("Error", "No se pudo actualizar la motocicleta");
            }
        });

        deleteBtn.setOnAction(e -> {
            if (currentMotorcycle == null) return;

            if (!currentMotorcycle.getStatus().equals("Vendida")) {
                showAlert("Error", "Solo se pueden eliminar motocicletas vendidas");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Está seguro de eliminar esta motocicleta?");
            confirm.setContentText("Esta acción no se puede deshacer");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                boolean success = inventoryController.deleteMotorcycle(currentMotorcycle.getPlate());
                if (success) {
                    showAlert("Éxito", "Motocicleta eliminada del sistema");
                    form.setVisible(false);
                    searchField.clear();
                    currentMotorcycle = null;
                } else {
                    showAlert("Error", "No se pudo eliminar la motocicleta");
                }
            }
        });

        cancelBtn.setOnAction(e -> {
            form.setVisible(false);
            searchField.clear();
            currentMotorcycle = null;
        });

        backBtn.setOnAction(e -> mainView.show());

        root.getChildren().addAll(titleLabel, searchPanel, form, buttonBox);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5;");

        return new Scene(scrollPane, 700, 750);
    }

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(250);
        field.setStyle("-fx-font-size: 14;");
        return field;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        label.setStyle("-fx-text-fill: #34495e;");
        return label;
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(120);
        button.setPrefHeight(40);
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
