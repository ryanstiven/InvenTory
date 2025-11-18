import javafx.application.Application;
import javafx.stage.Stage;
import controllers.*;
import views.MainView;

public class Main extends Application {
    private InventoryController inventoryController;
    private OrderController orderController;
    private SaleController saleController;

    @Override
    public void start(Stage primaryStage) {
        inventoryController = new InventoryController();
        orderController = new OrderController();
        saleController = new SaleController();

        MainView mainView = new MainView(primaryStage, inventoryController, 
                                        orderController, saleController);

        primaryStage.setTitle("InvenTory - Sistema de Gestión de Motocicletas");
        primaryStage.setScene(mainView.createScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
