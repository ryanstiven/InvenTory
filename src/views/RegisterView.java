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
import utils.Validator;
import utils.Formatter;

public class RegisterView {
    private Stage stage;
    private InventoryController inventoryController;
    private MainView mainView;

    public RegisterView(Stage stage, InventoryController inventoryController, MainView mainView) {
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
        Label titleLabel = new Label("Registrar Nueva Motocicleta");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Formulario
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // Campos
        TextField plateField = createTextField("ABC-1234");
        TextField brandField = createTextField("Honda");
        TextField modelField = createTextField("CBR 600RR");
        TextField yearField = createTextField("2023");
        TextField priceField = createTextField("15000.00");
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Disponible", "Reservada");
        statusCombo.setValue("Disponible");
        statusCombo.setPrefWidth(250);
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Deportiva", "Touring", "Cruiser", "Naked", "Enduro", "Scooter");
        categoryCombo.setValue("Deportiva");
        categoryCombo.setPrefWidth(250);

        // Agregar al formulario
        int row = 0;
        form.add(createLabel("Placa:"), 0, row);
        form.add(plateField, 1, row++);
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

        Button registerBtn = createButton("Registrar", "#27ae60");
        Button clearBtn = createButton("Limpiar", "#95a5a6");
        Button backBtn = createButton("Volver", "#34495e");

        buttonBox.getChildren().addAll(registerBtn, clearBtn, backBtn);

        // Eventos
        registerBtn.setOnAction(e -> {
            String plate = Formatter.formatPlate(plateField.getText());
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String yearStr = yearField.getText().trim();
            String priceStr = priceField.getText().trim();
            String status = statusCombo.getValue();
            String category = categoryCombo.getValue();

            // Validaciones
            if (!Validator.isValidPlate(plate)) {
                showAlert("Error", "Placa inválida. Use formato: ABC-1234");
                return;
            }
            if (!Validator.isValidString(brand)) {
                showAlert("Error", "La marca es obligatoria");
                return;
            }
            if (!Validator.isValidString(model)) {
                showAlert("Error", "El modelo es obligatorio");
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
            
            if (!Validator.isNumeric(priceStr)) {
                showAlert("Error", "El precio debe ser numérico");
                return;
            }
            
            double price = Double.parseDouble(priceStr);
            if (!Validator.isValidPrice(price)) {
                showAlert("Error", "El precio debe ser mayor a 0");
                return;
            }

            // Registrar
            boolean success = inventoryController.registerMotorcycle(
                plate, brand, model, year, price, status, category
            );

            if (success) {
                showAlert("Éxito", "Motocicleta registrada correctamente\nPlaca: " + plate);
                clearFields(plateField, brandField, modelField, yearField, priceField);
                statusCombo.setValue("Disponible");
                categoryCombo.setValue("Deportiva");
            } else {
                showAlert("Error", "La placa ya existe en el inventario");
            }
        });

        clearBtn.setOnAction(e -> {
            clearFields(plateField, brandField, modelField, yearField, priceField);
            statusCombo.setValue("Disponible");
            categoryCombo.setValue("Deportiva");
        });

        backBtn.setOnAction(e -> mainView.show());

        root.getChildren().addAll(titleLabel, form, buttonBox);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5;");

        return new Scene(scrollPane, 650, 700);
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

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
