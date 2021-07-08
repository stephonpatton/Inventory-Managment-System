package View_Controller;

import Model.*;
import static Model.Inventory.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainController extends Application implements Initializable {
    //Table of products
    @FXML private TableView<Product> productTableView;
    //Product id column
    @FXML private  TableColumn<Product,Integer> productIdCol;
    //Product name column
    @FXML private  TableColumn<Product,String> productNameCol;
    //Product inventory column
    @FXML private  TableColumn<Product,Integer> productInvCol;
    //Product price column
    @FXML private  TableColumn<Product,Double> productPriceCol;
    //Table of parts
    @FXML private TableView<Part> partsTableView;
    //Part id column
    @FXML private TableColumn<Part, Integer> partIdCol;
    //Part name column
    @FXML private TableColumn<Part, String> partNameCol;
    //Part inventory column
    @FXML private TableColumn<Part, Integer> partInvCol;
    //Part price column
    @FXML private TableColumn<Part, Double> partPriceCol;

    //Search text field for part (main screen)
    @FXML private TextField mainPartSearchField;
    //Search text field for product (main screen)
    @FXML private TextField mainProductSearchField;

    //Temporary variable to find out what part to modify
    private static Part tempPart;
    //Temporary variable to find out what part index is being modified
    private static int tempPartIndex;

    //Temporary variable to find out what product to modify
    private static Product tempProduct;
    //Temporary variable to find out what product index is being modified
    private static int tempProductIndex;

    /**
     * Starts application and loads up main screen (mainform.fxml)
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
        //End of sample data creation

        //Adding sample products to inventory
        Inventory.addProduct(bike);
        Inventory.addProduct(motorcycle);

        //Adding sample parts to inventory
        Inventory.addPart(axle);
        Inventory.addPart(tires);
        Inventory.addPart(brakes);
        Inventory.addPart(windows);
        Inventory.addPart(wipers);

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
            //Load in product table
            updateProductsTable();
            //Load in part table
            updatePartsTable();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        //Setting product columns to cell factory to get data from application
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Setting part columns to cell factory to get data from application
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
            //Setting scene to AddPart window and setting properties for it
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/AddPart.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.show();

            //Hides current window
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


    /**
     * Opens the add product window upon a click
     * @param actionEvent button being pressed
     */
    public void openAddProductWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            //Setting new scene and adding properties to scene
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/AddProduct.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root, 920, 500));
            stage.setResizable(false);
            stage.show();

//            Hides current window after scene has been set
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
     * Updates the parts table based on inventory
     */
    public void updatePartsTable() {
        partsTableView.setItems(Inventory.getAllParts());
    }

    /**
     * Updates the products table based on inventory
     */
    public void updateProductsTable() {
        productTableView.setItems(Inventory.getAllProducts());
    }

    /**
     * Gets the selected part object in a row in the parts table (used for modifying part)
     * @param event when modify button is pressed
     */
    public void getSelectedPart(ActionEvent event) {
        try {
            //Getting the part that needs to be modified
            if(partsTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please select a part to modify");
                alert.setContentText("A part must be selected in order to modify");
                alert.showAndWait().ifPresent(response -> {

                });
            } else {
                tempPart = partsTableView.getSelectionModel().getSelectedItem();
                //Setting the index
                tempPartIndex = getAllParts().indexOf(tempPart);
                //Setting the scene
                Parent partsModify = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPart.fxml")));
                Scene scene = new Scene(partsModify);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch(Exception e) {
            System.err.println("\nPlease select a part to modify");
        }
    }

    /**
     * Gets the selected product object in a row in the products table to modify
     * @param event when modify button is pressed for product
     */
    public void getSelectedProduct(ActionEvent event) {
        try {
            //Getting the product that needs to be modified
            if(productTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please select product to modify");
                alert.setContentText("A product must be selected in order to modify");
                alert.showAndWait().ifPresent(response -> {

                });
            } else {
                tempProduct = productTableView.getSelectionModel().getSelectedItem();
                //Setting the modify index
                tempProductIndex = getAllProducts().indexOf(tempProduct);
                //Setting the scene
                Parent productModify = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyProduct.fxml")));
                Scene scene = new Scene(productModify);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch(Exception e) {
            System.err.println("\nPlease select a product to modify");
        }
    }

    /**
     * Gets the part that is currently being modified
     * @return Part being modified
     */
    public static Part partToModify() {
        return tempPart;
    }

    /**
     * Gets the product that is currently being modified
     * @return Product being modified
     */
    public static Product productToModify() {
        return tempProduct;
    }

    /**
     * Gets the index of the product being modified
     * @return Index of product being modified
     */
    public static int productIndexToModify() {
        return tempProductIndex;
    }

    /**
     * Gets the index of the part to modify
     * @return Index of part to modify
     */
    public static int partIndexToModify() {
        return tempPartIndex;
    }

    //TODO: Confirmation dialog for deletion

    /**
     * Deletes a part from the table after the delete button is pressed
     */
    public void deletePartFromTable() {
        //Gets the part from the table
        if(partsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please select a part to delete");
            alert.setContentText("A part must be selected in order to delete");
            alert.showAndWait().ifPresent(response -> {

            });
        } else {
            tempPart = partsTableView.getSelectionModel().getSelectedItem();
            //Buttons for alert window
            ButtonType deleteButton = new ButtonType("Delete");
            ButtonType cancelButton = new ButtonType("Cancel");
            //Creating alert windows
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the part?", deleteButton, cancelButton);
            //Handling alert window response
            alert.showAndWait().ifPresent(response -> {
                if (response == deleteButton) {
                    tempPartIndex = getAllParts().indexOf(tempPart);
                    if (deletePart(tempPart)) {
                        System.out.println("Successfully deleted");
                    } else {
                        System.out.println("Was not deleted");
                    }
                } else if (response == cancelButton) {
                    alert.close();
                }
            });
        }
    }

    /**
     * Deletes a product from the table after the delete button is pressed
     */
    public void deleteProductFromTable() {
        //Gets product from table
        if(productTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please select a product to delete from the inventory");
            alert.setContentText("A product must be selected in order to delete from inventory");
            alert.showAndWait().ifPresent(response -> {

            });
        } else {
            tempProduct = productTableView.getSelectionModel().getSelectedItem();
            //Checks to see if product has associated parts
            if (tempProduct.getAssociatedParts().size() != 0) {
                ButtonType okayButton = new ButtonType("Okay");
                Alert deleteParts = new Alert(Alert.AlertType.ERROR, "Please delete associated parts first", okayButton);
                deleteParts.setHeaderText("This product has parts associated with it");
                deleteParts.show();
            } else {
                //Deletes product is no associated parts
                tempProductIndex = getAllProducts().indexOf(tempProduct);
                if (deleteProduct(tempProduct)) {
                    System.out.println("Successfully deleted product");
                } else {
                    System.out.println("Was not deleted");
                }
            }
        }
    }

    /**
     * Searches part by ID based on a query search from user
     * @param query given by user
     * @return List of Parts that match query
     */
    public ObservableList<Part> searchPartById(String query) {
        //Creates temporary list for results
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        //Checks to see if query matches any id in the inventory
        for(Part part : Inventory.getAllParts()) {
            if((part.getId() == Integer.parseInt(query))) {
                searchPart.add(part);
            }
        }
        //Checks if there is no match
        if(searchPart.size() == 0 ) {
            System.out.println("No ID matches for part");
        }
        return searchPart;
    }

    /**
     * Searches parts by name based on a query search from user
     * @param query given by user
     * @return List of Parts that match query
     */
    public ObservableList<Part> searchPartByName(String query) {
        //Creates temporary list for results
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        //Checks to see if query matches any name (or partial name) in inventory
        for(Part part : Inventory.getAllParts()) {
            if(part.getName().toLowerCase().contains(query.toLowerCase())) {
                searchPart.add(part);
            }
        }
        //Checks if there is no match
        if(searchPart.size() == 0 ) {
            System.out.println("No results for part name");
        }
        return searchPart;
    }

    /**
     * Searches part with query using searchById and searchByName after enter is pressed
     */
    public void searchPart() {
        //Gets query to process
        String query = mainPartSearchField.getText();
        //Result list
        ObservableList<Part> idList;
        //Checks if query is number
        if(query.matches("[0-9]*") && query.length() != 0) {
            idList = searchPartById(query);
            if(idList.size() == 0) {
                updatePartsTable();
            } else {
                partsTableView.setItems(idList);
            }
        } else {
            //checks if query is string (for name)
            ObservableList<Part> tmpList = searchPartByName(query);
            if(tmpList.size() == 0) {
                updatePartsTable();
            } else {
                partsTableView.setItems(tmpList);
            }
        }
    }

    /**
     * Searches inventory for products based on ID query
     * @param query given by user
     * @return List of Product results
     */
    public ObservableList<Product> searchProductById(String query) {
        //Temporary list of results
        ObservableList<Product> idList = FXCollections.observableArrayList();

        //Checks if there is a product id match
        for(Product product : Inventory.getAllProducts()) {
            if((product.getId() == Integer.parseInt(query))) {
                idList.add(product);
            }
        }
        //Checks if there was no matches
        if(idList.size() == 0 ) {
            //TODO: Display alert box
            System.out.println("No ID matches for product");
        }
        return idList;
    }

    public ObservableList<Product> searchProductByName(String query) {
        //Temporary list of results
        ObservableList<Product> searchProduct = FXCollections.observableArrayList();
        //Checks if there is a product name (or partial name) match
        for(Product product : Inventory.getAllProducts()) {
            if(product.getName().toLowerCase().contains(query.toLowerCase())) {
                searchProduct.add(product);
            }
        }
        //Checks if there are no results
        if(searchProduct.size() == 0) {
            System.out.println("No results for product name");
        }
        return searchProduct;
    }

    /**
     * Searches products in inventory after enter is pressed. Combines searchByID and searchByName
     */
    public void searchProduct() {
        //Gets query for search field
        String query = mainProductSearchField.getText();
        //Creates temporary list for results
        ObservableList<Product> idList;
        //Checks if query is a number
        if(query.matches("[0-9]*") && query.length() != 0) {
            idList = searchProductById(query);
            if(idList.size() == 0) {
                updateProductsTable();
            } else {
                productTableView.setItems(idList);
            }
        } else {
            //Creates temporary list
            ObservableList<Product> tmpList = searchProductByName(query);
            //Checks if results were not found
            if(tmpList.size() == 0) {
                updateProductsTable();
            } else {
                productTableView.setItems(tmpList);
            }
        }
    }
}
