package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;

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

public class AddProductController implements Initializable {
    //Keeps track of current product ID count
    private int productID;
    //Text field for product ID
    @FXML private TextField addProductIdTF;
    //Text field for product name
    @FXML private TextField addProductName;
    //Text field for product inventory
    @FXML private TextField addProductInv;
    //Text field for product price
    @FXML private TextField addProductPrice;
    //Text field for product max
    @FXML private TextField addProductMax;
    //Text field for product min
    @FXML private TextField addProductMin;

    //Table with available parts to associate
    @FXML private TableView<Part> availablePartTableView;
    //Available parts id column
    @FXML private TableColumn<Part, Integer> availablePartsPartIdCol;
    //Available parts name column
    @FXML private TableColumn<Part, String> availablePartsNameCol;
    //Available parts inventory column
    @FXML private TableColumn<Part, Integer> availablePartsInvCol;
    //Available parts price column
    @FXML private TableColumn<Part, Double> availablePartsPriceCol;

    //Table with associated parts
    @FXML private TableView<Part> addProductAddedPartsTableView;
    //Associated parts id column
    @FXML private TableColumn<Part, Integer> addedPartsId;
    //Associated parts name column
    @FXML private TableColumn<Part, String> addedPartsName;
    //Associated parts inventory column
    @FXML private TableColumn<Part, Integer> addedPartsInv;
    //Associated parts price column
    @FXML private TableColumn<Part, Double> addedPartsPrice;
    //Search parts for product text field
    @FXML private TextField addProductPartsTF;

    //Gets part to add to product
    private Part partToAdd;
    //Gets part to remove from product
    private Part partToRemove;
    //Temporary list for part adding feature
    ObservableList<Part> tempList = FXCollections.observableArrayList();

    //Boolean variables to check if fields have valid data
    private boolean minCheck;
    private boolean maxCheck;
    private boolean priceCheck;
    private boolean invCheck;
    private boolean nameCheck;


    /**
     * Returns to the main screen once exit button is pressed on the Add Product screen
     * @param actionEvent Exit button pressed
     */
    public void cancelAddProduct(ActionEvent actionEvent) {
        Parent root;
        try {
            // Setting up main screen and properties
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/mainform.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Main Page");
            stage.setScene(new Scene(root, 1100, 500));
            stage.setResizable(false);
            stage.show();
            // Hide this current window
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and adds the product after all of the validation checks have been performed
     * @return The new product that was created
     */
    public boolean createProduct() {
        boolean created = false;
//            presentErrors();
            //Create product object that is being added to inventory
            Product newProduct;
            //Setting attributes based on information from user
            String productName = addProductName.getText();
            try {
                int productStock = Integer.parseInt(addProductInv.getText());
                double productPrice = Double.parseDouble(addProductPrice.getText());
                int productMin = Integer.parseInt(addProductMin.getText());
                int productMax = Integer.parseInt(addProductMax.getText());
                checkIfInvValid();
                if(minCheck && maxCheck && invCheck) {
                    newProduct = new Product(Inventory.getProductIDCount(), productName, productPrice, productStock, productMax, productMin);
                    addAllPartsToProduct(tempList, newProduct);
                    //Adding to inventory
                    Inventory.addProduct(newProduct);
                    created = true;
                    setAllChecksToFalse();
                } else {

                }
            } catch (Exception e) {
                presentErrors();
                System.err.println("Please provide numbers only");
            }
        return created;

    }
//        //Create product object that is being added to inventory
//        Product newProduct;
//        //Setting attributes based on information from user
//        String productName = addProductName.getText();
//        int productStock = Integer.parseInt(addProductInv.getText());
//        double productPrice = Double.parseDouble(addProductPrice.getText());
//        int productMin = Integer.parseInt(addProductMin.getText());
//        int productMax = Integer.parseInt(addProductMax.getText());
//        newProduct = new Product(Inventory.getProductIDCount(), productName, productPrice, productStock, productMax, productMin);
//        //Adding to inventory
//        Inventory.addProduct(newProduct);
//        return newProduct;



    /**
     * Checks if product price has data and if the data is valid (numbers only)
     * @return True if data is valid
     */
    public boolean checkAddProductPrice() {
        if((addProductPrice.getText().matches("[0-9]*") || Double.parseDouble(addProductPrice.getText()) % 1 != 0) && addProductPrice.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide numbers for the price.");
            return false;
        }
    }

    /**
     * Checks if product min has data and if the data is valid
     * @return True if the data is valid
     */
    public boolean checkAddProductMin() {
        if(addProductMin.getText().matches("[0-9]*") && addProductMin.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for min");
            return false;
        }
    }

    /**
     * Checks if product max has data and if the data is valid
     * @return True if the data is valid
     */
    public boolean checkAddProductMax() {
        if(addProductMax.getText().matches("[0-9]*") && addProductMax.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for max");
            return false;
        }
    }

    /**
     * Checks if product inventory has data and if the data is valid
     * @return True if the data is valid
     */
    public boolean checkAddProductInv() {
        if(addProductInv.getText().matches("[0-9]*") && addProductInv.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please use numbers only");
            return false;
        }
    }

    /**
     * Checks if product name has data and if the data is valid
     * @return True if the data is valid
     */
    public boolean checkAddProductName() {
        if(addProductName.getLength() == 0 || addProductName.getText().matches("[0-9]*")) {
            System.out.println("Please provide a name");
            //TODO: Alert boxes for all errors
            return false;
        } else {
            return true;
        }
    }

    private boolean areFieldsValid() {
        return checkAddProductMin() && checkAddProductMax() && checkAddProductPrice() && checkAddProductInv()
                && checkAddProductName();
    }

    /**
     * The initial method that is called when the screen is loaded. Populates table and auto gens ID
     * @param url Required
     * @param resourceBundle Required
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Gets the current product count to generate a product ID for new product
        productID = Inventory.getProductIDCount();
        addProductIdTF.setText("AUTO GEN: " + productID);
        try {
            populateProductTable();
        }catch(Exception e) {
            System.err.println("No data to populate data");
        }
    }

    /**
     * Populates the product table with the current products in the inventory
     */
    public void populateProductTable() {
        //Setting table with data from inventory
        availablePartTableView.setItems(Inventory.getAllParts());

        availablePartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    /**
     * Checks if all input is valid and then calls createProduct method to create and add product
     * @param actionEvent Save button pressed
     */
    public void addProductSubmit(ActionEvent actionEvent) {
//        //Checks if all data fields have valid data
//        if(checkAddProductName() && checkAddProductInv() && checkAddProductPrice()
//                && checkAddProductMax() && checkAddProductMin()) {
//            //Creates product and adds associated parts to it
//            Product product = createProduct();
//            addAllPartsToProduct(tempList,product);
//            cancelAddProduct(actionEvent);
//        }
//        if(!areFieldsValid()) {
//            checkAllFields();
//            presentErrors();
//        } else {
//            if(createProduct()) {
//                cancelAddProduct(actionEvent);
//            } else {
//                setAllChecksToFalse();
//            }
//        }

        if(areFieldsValid()) {
            if(createProduct()) {
                cancelAddProduct(actionEvent);
            } else {
                setAllChecksToFalse();
            }
        } else {
            checkAllFields();
            checkIfInvValid();
            presentErrors();
            System.out.println("Please enter valid data");
        }
    }


    /**
     * Adds all parts from associated table to the product
     * @param list Parts list to add to product
     * @param product Product that the list is being added to
     */
    private void addAllPartsToProduct(ObservableList<Part> list, Product product) {
        //Filters and adds all parts to product
        for(Part part : list) {
            product.addAssociatePart(part);
        }
    }

    /**
     * Adds part from available parts table to associated parts table to be added to product
     */
    public void addPartToProduct() {
        //Get part to add
        partToAdd = availablePartTableView.getSelectionModel().getSelectedItem();
        //Add to tempList to add to product
        tempList.add(partToAdd);

        //Set added parts table
        addProductAddedPartsTableView.setItems(tempList);
        addedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Remove part from associated parts table
     */
    public void removeAddedParts() {
        //Get part to remove from product/table
        partToRemove = addProductAddedPartsTableView.getSelectionModel().getSelectedItem();

        //Setting up and show alert dialog for confirmation
        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete part association", deleteButton, cancelButton);
        alert.setContentText("Are you sure you want to remove the part association?");
        alert.setHeaderText("Delete part association");
        alert.showAndWait().ifPresent(response -> {
            if(response == deleteButton) {
                tempList.remove(partToRemove);
            } else if(response == cancelButton) {
                alert.close();
            }
        });
    }

    /**
     * Searches parts by an ID given by user
     * @param query Provided by user
     * @return Matching part based on query
     */
    public ObservableList<Part> searchPartById(String query) {
        //Temporary list for results
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        //Filter through inventory to try to get a match
        for(Part part : Inventory.getAllParts()) {
            if((part.getId() == Integer.parseInt(query))) {
                searchPart.add(part);
            }
        }
        //Checks if no matches were found
        if(searchPart.size() == 0 ) {
            System.out.println("No ID matches for part");
        }
        return searchPart;
    }

    /**
     * Searches parts by name (or partial name) given by user
     * @param query Given by user
     * @return Matching part based on query
     */
    public ObservableList<Part> searchPartByName(String query) {
        //Temporary list for results
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        //Filter through inventory to try and find a match or partial match for name
        for(Part part : Inventory.getAllParts()) {
            if(part.getName().toLowerCase().contains(query.toLowerCase())) {
                searchPart.add(part);
            }
        }
        //Checks if no matches were found
        if(searchPart.size() == 0 ) {
            System.out.println("No results for part name");
        }
        return searchPart;
    }

    /**
     * Combines searchByName and searchByID to search for anything given by user
     */
    public void searchPart() {
        //Gets query from search box
        String query = addProductPartsTF.getText();
        //Create temporary list for results
        ObservableList<Part> idList;
        //Checks if query is searching for ID
        if(query.matches("[0-9]*") && query.length() != 0) {
            idList = searchPartById(query);
            if(idList.size() == 0) {
                updatePartsTable();
            } else {
                availablePartTableView.setItems(idList);
            }
        } else {
            //Checks if query is searching for name or partial name
            ObservableList<Part> tmpList = searchPartByName(query);
            if(tmpList.size() == 0) {
                updatePartsTable();
            } else {
                availablePartTableView.setItems(tmpList);
            }
        }
    }

    /**
     * Updates parts table with current inventory of parts
     */
    public void updatePartsTable() {
        availablePartTableView.setItems(Inventory.getAllParts());
    }

    /**
     * Checks all text fields to see which fields contain errors
     */
    private void checkAllFields() {
        checkPriceField();
        checkInvField();
        checkMaxField();
        checkMinField();
        checkNameField();
    }

    private void checkNameField() {
        String input = addProductName.getText();
        if(input.matches(".*\\d.*") || input.length() == 0){
//        if(input.matches("[0-9]*") || input.length() == 0){
            nameCheck = false;
            System.err.println("Please exclude numbers or empty data fields");
        } else {
            nameCheck = true;
        }
    }

    private void checkMinField() {
        String input = addProductMin.getText();
        int tryInt;
        try {
            tryInt = Integer.parseInt(input.trim());
            minCheck = true;
            System.out.println("minCheck changed to " + minCheck);
        }catch(Exception e) {
            System.err.println("Please provide a number for min");
            minCheck = false;
            System.out.println("minCheck changed to " + minCheck);
        }
        System.out.println("value of minCheck " + minCheck);
    }

    private void checkMaxField() {
        String input = addProductMax.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            maxCheck = true;
        }catch(Exception e) {
            maxCheck = false;
            System.err.println("Please provide a number for max");
        }
    }

    private void checkInvField() {
        String input = addProductInv.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            invCheck = true;
        }catch (Exception e) {
            System.err.println("Please provide a number for inventory");
            invCheck = false;
        }
    }

    private void checkPriceField() {
        String input = addProductPrice.getText();
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
        }catch(Exception e) {
            System.err.println("Please provide a number for price");
            priceCheck = false;
        }
    }

    private void noResultAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {

        });
    }

    /**
     * Changes the color of the text fields that has invalid data
     */
    public void presentErrors() {
        if(!maxCheck) {
            addProductMax.setStyle("-fx-border-color: #ae0700");
        } else {
            addProductMax.setStyle("-fx-border-color: #9f07");
        }

        if(!minCheck) {
            addProductMin.setStyle("-fx-border-color: #ae0700");
        } else {
            addProductMin.setStyle("-fx-border-color: #9f07");
        }
        if(!invCheck) {
            addProductInv.setStyle("-fx-border-color: #ae0700");
        } else {
            addProductInv.setStyle("-fx-border-color: #9f07");
        }

        if(!priceCheck) {
            addProductPrice.setStyle("-fx-border-color: #ae0700");
        } else {
            addProductPrice.setStyle("-fx-border-color: #9f07");
        }

        if(!nameCheck) {
            addProductName.setStyle("-fx-border-color: #ae0700");
        } else {
            addProductName.setStyle("-fx-border-color: #9f07");
        }
    }

    private void checkIfInvValid() {
        try {
            int min = Integer.parseInt(addProductMin.getText());
            int max = Integer.parseInt(addProductMax.getText());
            int inv = Integer.parseInt(addProductInv.getText());

            if(min > max) {
                minCheck = false;
                maxCheck = false;
            }

            if(inv < min) {
                invCheck = false;
                minCheck = false;
            }

            if(inv > max) {
                invCheck = false;
                maxCheck = false;
            }

            if((inv < max) && (inv > min)) {
                invCheck = true;
                maxCheck = true;
                minCheck = true;
            }
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Numbers only for inv, min, and max");
            alert.setContentText("Please only include numbers for inventory, min, and max");
            alert.showAndWait().ifPresent(response -> {

            });
        }
        if(!maxCheck || !minCheck || !invCheck) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Numbers only for inv, min, and max");
            alert.setContentText("Please only include numbers for inventory, min, and max");
            alert.showAndWait().ifPresent(response -> {

            });
        }
        presentErrors();
    }

    /**
     * Resets all error checks to false after error checking has happened (to restart process for next click)
     */
    private void setAllChecksToFalse() {
        maxCheck = false;
        minCheck = false;
        priceCheck = false;
        invCheck = false;
        nameCheck = false;
    }
    //TODO: Logical errors for inventory
}
