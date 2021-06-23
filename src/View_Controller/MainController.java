package View_Controller;

import Model.*;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static Model.Inventory.*;



public class MainController extends Application implements Initializable {
    @FXML private TableView<Product> productTableView;
    @FXML private  TableColumn<Product,Integer> productIdCol;
    @FXML private  TableColumn<Product,String> productNameCol;
    @FXML private  TableColumn<Product,Integer> productInvCol;
    @FXML private  TableColumn<Product,Integer> productPriceCol;
    @FXML private TableView<Part> partsTableView;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, Integer> partPriceCol;

    private static Part tempPart;
    private static int tempPartIndex;

    /**
     * Starts application and loads up main page (fxml)
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/mainform.fxml")));
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main method for the entire program. Loads in sample data and uses FXML launch command to start
     * @param args
     */
    //TODO: Tell evaluators where javadocs folder is
    //TODO: Comment everything
    public static void main(String[] args) {
        //Below is sample data for the application to load (includes parts and products)
        Product bike = new Product(1, "Bike", 2, 1, 0, 20);
        Product motorcycle = new Product(2, "Motorcycle", 2, 1, 0, 20);

        Part axle = new InhousePart(1, "Axle", 204.99, 1, 1, 20, 200);
        Part tires = new InhousePart(2, "Tires", 102.90, 200, 1, 20, 10);
        Part brakes = new InhousePart(3, "Brakes", 15.99, 1000, 1, 20, 1);

        Part windows = new OutsourcedPart(4, "Windows", 529, 3, 1, 50, "Glass R US");
        Part wipers = new OutsourcedPart(5, "Wipers", 9.99, 52, 1, 760, "Wipers R US");

        //Adding Products to inventory
        Inventory.addProduct(bike);
        Inventory.addProduct(motorcycle);

        //Adding parts to inventory
        Inventory.addPart(axle);
        Inventory.addPart(tires);
        Inventory.addPart(brakes);
        Inventory.addPart(windows);
        Inventory.addPart(wipers);
//
//        Inventory.updateProduct(1,new Product(getProductIDCount(), "FUCK ME", 3, 2, 0, 10));

        launch(args);
    }

    /**
     * Loads in the controller that has the data for tables when the page is loaded
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            updateProductsTable();
            updatePartsTable();
        } catch(NullPointerException e) {

            e.printStackTrace();
        }
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Opens up the Add Part page when the add button is pressed
     * @param actionEvent
     */
    public void openAddPartWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/AddPart.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Maybe delete if fully functional later
    public void openModifyPartWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/ModifyPart.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Maybe delete if fully functional later
    public void openModifyProductWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/ModifyProduct.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root, 920, 500));
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void openAddProductWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/AddProduct.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root, 920, 500));
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes application when the exit button is pressed
     */
    public void closeApp() {
        Platform.exit();
    }

    /**
     * Updates the parts table (useful for changes in table)
     */
    public void updatePartsTable() {
        partsTableView.setItems(Inventory.getAllParts());
    }

    /**
     * Updates the products table (useful for changes in table)
     */
    public void updateProductsTable() {
        productTableView.setItems(Inventory.getAllProducts());
    }

    /**
     * Gets the selected part object in a row in the parts table (used for modifying part)
     * @param event when modify button is pressed
     * @throws IOException if a row is not selected
     */
    public void getSelectedRow(ActionEvent event) throws IOException {
//        boolean inHouse = partsTableView.getSelectionModel().getSelectedItem().getClass().getName().equals("Model.InHouse");
        try {
            tempPart = partsTableView.getSelectionModel().getSelectedItem();
            tempPartIndex = getAllParts().indexOf(tempPart);
            Parent partsModify = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPart.fxml")));
            Scene scene = new Scene(partsModify);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch(Exception e) {
            System.err.println("\nPlease select a part to modify");
        }
    }

    public static Part partToModify() {
        return tempPart;
    }

    /**
     * Gets the index of the part to modify
     * @return index of part to modify
     */
    public static int partIndexToModify() {
        return tempPartIndex;
    }

    //TODO: Confirmation dialog for deletion

    /**
     * Deletes a part from the table after the delete button is pressed
     */
    public void deletePartFromTable() {
        tempPart = partsTableView.getSelectionModel().getSelectedItem();
        tempPartIndex = getAllParts().indexOf(tempPart);
        if(deletePart(tempPart)) {
            //if you want to make auto gen id 1 less than before because of deletion
//            setPartCount(getPartIDCount() - 1);
            System.out.println("Successfully deleted");
        } else {
            System.out.println("Was not deleted");
        }
    }

    //TODO: Delete product from table implementation
    //TODO: Confirmation dialog for deletion

    /**
     * Deletes a product from the table after the delete button is pressed
     */
    public void deleteProductFromTable() {

    }
}
