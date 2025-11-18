package views;

import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import controllers.InventoryController;
import models.Motorcycle;
import structures.LinkedList;
import utils.Formatter;

public class SearchView {
    private Stage stage;
    private InventoryController inventoryController;
    private MainView mainView;
    private TableView<Motorcycle> table;

    public SearchView(Stage stage, InventoryController inventoryController, MainView mainView) {
        this.stage = stage;
        this.inventoryController = inventoryController;
        this.mainView = mainView;
    }

    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Título
        Label titleLabel = new Label("Buscar y Filtrar Motocicletas");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Panel de búsqueda
        VBox searchPanel = new VBox(15);
        searchPanel.setPadding(new Insets(20));
        searchPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER);

        ComboBox<String> fieldCombo = new ComboBox<>();
        fieldCombo.getItems().addAll("Placa", "Marca", "Modelo", "Año", "Precio Max", "Estado", "Categoría");
        fieldCombo.setValue("Placa");
        fieldCombo.setPrefWidth(150);

        TextField searchField = new TextField();
        searchField.setPromptText("Valor de búsqueda...");
        searchField.setPrefWidth(250);

        Button searchBtn = createButton("Buscar", "#3498db");
        Button showAllBtn = createButton("Mostrar Todo", "#9b59b6");
        Button clearBtn = createButton("Limpiar", "#95a5a6");

        searchBox.getChildren().addAll(new Label("Campo:"), fieldCombo, searchField, searchBtn, showAllBtn, clearBtn);
        searchPanel.getChildren().add(searchBox);

        // Tabla de resultados
        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setPrefHeight(400);

        TableColumn<Motorcycle, String> plateCol = new TableColumn<>("Placa");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("plate"));
        plateCol.setPrefWidth(100);

        TableColumn<Motorcycle, String> brandCol = new TableColumn<>("Marca");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(120);

        TableColumn<Motorcycle, String> modelCol = new TableColumn<>("Modelo");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(150);

        TableColumn<Motorcycle, Integer> yearCol = new TableColumn<>("Año");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Motorcycle, Double> priceCol = new TableColumn<>("Precio");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(120);

        TableColumn<Motorcycle, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<Motorcycle, String> categoryCol = new TableColumn<>("Categoría");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(120);

        table.getColumns().addAll(plateCol, brandCol, modelCol, yearCol, priceCol, statusCol, categoryCol);

        // Estadísticas
        Label statsLabel = new Label("Total de resultados: 0");
        statsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statsLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Botón volver
        Button backBtn = createButton("Volver al Menú", "#34495e");
        backBtn.setPrefWidth(200);

        // Eventos
        searchBtn.setOnAction(e -> performSearch(fieldCombo.getValue(), searchField.getText(), statsLabel));
        showAllBtn.setOnAction(e -> showAllMotorcycles(statsLabel));
        clearBtn.setOnAction(e -> {
            searchField.clear();
            table.getItems().clear();
            statsLabel.setText("Total de resultados: 0");
        });
        backBtn.setOnAction(e -> mainView.show());

        root.getChildren().addAll(titleLabel, searchPanel, table, statsLabel, backBtn);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5;");

        return new Scene(scrollPane, 900, 700);
    }

    private void performSearch(String field, String value, Label statsLabel) {
        if (value == null || value.trim().isEmpty()) {
            showAlert("Error", "Ingrese un valor de búsqueda");
            return;
        }

        table.getItems().clear();

        if (field.equals("Placa")) {
            Motorcycle motorcycle = inventoryController.searchByPlate(value.toUpperCase().trim());
            if (motorcycle != null) {
                table.getItems().add(motorcycle);
                statsLabel.setText("Total de resultados: 1");
            } else {
                statsLabel.setText("Total de resultados: 0");
                showAlert("No encontrado", "No se encontró motocicleta con esa placa");
            }
        } else {
            String searchField = convertFieldName(field);
            LinkedList<Motorcycle> results = inventoryController.searchByCriteria(searchField, value);
            
            for (int i = 0; i < results.size(); i++) {
                table.getItems().add(results.get(i));
            }
            
            statsLabel.setText("Total de resultados: " + results.size());
            
            if (results.isEmpty()) {
                showAlert("No encontrado", "No se encontraron resultados para la búsqueda");
            }
        }
    }

    private void showAllMotorcycles(Label statsLabel) {
        table.getItems().clear();
        LinkedList<Motorcycle> all = inventoryController.getAllMotorcycles();
        
        for (int i = 0; i < all.size(); i++) {
            table.getItems().add(all.get(i));
        }
        
        statsLabel.setText("Total de resultados: " + all.size());
    }

    private String convertFieldName(String field) {
        switch (field) {
            case "Marca": return "marca";
            case "Modelo": return "modelo";
            case "Año": return "año";
            case "Precio Max": return "precio";
            case "Estado": return "estado";
            case "Categoría": return "categoria";
            default: return field.toLowerCase();
        }
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(120);
        button.setPrefHeight(35);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                       "-fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        return button;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
