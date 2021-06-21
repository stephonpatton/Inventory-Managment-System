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
    @FXML
    private TableView<Product> productTableView;

    @FXML
    private  TableColumn<Product,Integer> productIdCol;
    @FXML
    private  TableColumn<Product,String> productNameCol;
    @FXML
    private  TableColumn<Product,Integer> productInvCol;
    @FXML
    private  TableColumn<Product,Integer> productPriceCol;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, Integer> partPriceCol;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/mainform.fxml")));
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Below is sample data for the application to load (includes parts and products)
        Product bike = new Product(1, "Bike", 2, 1, 0, 20);
        Product motorcycle = new Product(2, "Motorcycle", 2, 1, 0, 20);

        Part axle = new InHouse(1, "Axle", 204.99, 1, 1, 20, 200);
        Part tires = new InHouse(2, "Tires", 102.90, 200, 1, 20, 10);
        Part brakes = new InHouse(3, "Brakes", 15.99, 1000, 1, 20, 1);

        Part windows = new Outsourced(4, "Windows", 529, 3, 1, 50, "Glass R US");
        Part wipers = new Outsourced(5, "Wipers", 9.99, 52, 1, 760, "Wipers R US");

        //Adding Products to inventory
        Inventory.addProduct(bike);
        Inventory.addProduct(motorcycle);

        //Adding parts to inventory
        Inventory.addPart(axle);
        Inventory.addPart(tires);
        Inventory.addPart(brakes);
        Inventory.addPart(windows);
        Inventory.addPart(wipers);

//        Inventory.updateProduct(1,new Product(3, "FUCK ME", 3, 2, 0, 10));

        launch(args);
    }

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

    public void closeApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void updatePartsTable() {
        partsTableView.setItems(Inventory.getAllParts());
    }

    public void updateProductsTable() {
        productTableView.setItems(Inventory.getAllProducts());
    }
}
