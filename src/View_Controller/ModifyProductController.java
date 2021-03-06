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

import static View_Controller.MainController.productIndexToModify;
import static View_Controller.MainController.productToModify;

public class ModifyProductController implements Initializable {
    //Gets productID from inventory
    private int productId;

    //Text field for product ID
    @FXML private TextField modifyProductIdTF;
    //Text field for product name
    @FXML private TextField modifyProductNameTF;
    //Text field for product inventory
    @FXML private TextField modifyProductInvTF;
    //Text field for product price
    @FXML private TextField modifyProductPriceTF;
    //Text field for product max
    @FXML private TextField modifyProductMaxTF;
    //Text field for product min
    @FXML private TextField modifyProductMinTF;

    //Available parts table
    @FXML private TableView<Part> availablePartTableView;
    //Available parts ID column
    @FXML private TableColumn<Part, Integer> availablePartsPartIdCol;
    //Available parts name column
    @FXML private TableColumn<Part, String> availablePartsNameCol;
    //Available parts inventory column
    @FXML private TableColumn<Part, Integer> availablePartsInvCol;
    //Available parts price column
    @FXML private TableColumn<Part, Double> availablePartsPriceCol;

    //Associated parts table
    @FXML private TableView<Part> addedPartsTable;
    //Associated parts id column
    @FXML private TableColumn<Part, Integer> addedPartsId;
    //Associated parts name column
    @FXML private TableColumn<Part, String> addedPartsName;
    //Associated parts inventory column
    @FXML private TableColumn<Part, Integer> addedPartsInv;
    //Associated parts price column
    @FXML private TableColumn<Part, Double> addedPartsPrice;
    //Search text field for parts
    @FXML private TextField modifyProductPartTF;

    //Error label box
    @FXML private Label modifyProductErrorBox;

    //Gets the part being removed from product
    private Part partToRemove;
    //Gets part to be added to product
    private Part partToAdd;
    //Gets the index of the product being modified
    int indexOfProduct = productIndexToModify();


    //Boolean variables for field error highlighting
    private boolean nameCheck;
    private boolean invCheck;
    private boolean priceCheck;
    static boolean minCheck;
    static boolean maxCheck;


    /**
     * Returns to main screen if the cancel button is pressed
     * @param actionEvent Cancel button pressed on ModifyProduct screen
     */
    public void cancelModifyProduct(ActionEvent actionEvent)  {
        Parent root;
        try {
            //Sets the scene for main screen
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
     * Populates the available parts table with current parts from the inventory
     */
    public void populateProductTable() {
        //Gets parts from inventory and sets them to table view
        availablePartTableView.setItems(Inventory.getAllParts());

        availablePartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Initial method called when ModifyProduct screen is loaded. Populates tables with data in inventory
     * @param url Required
     * @param resourceBundle Required
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Populate fields with data in inventory
            populateFields();
        } catch(Exception e) {
            System.err.println("No data to populate fields");
        }
        try {
            //Populates parts tables
            populateProductTable();
            populateAddedPartsTable();
        }catch(Exception e) {
            System.err.println("No data to populate data");
        }
    }

    /**
     * Populates text fields with data in inventory based on product being modified
     */
    public void populateFields() {
        //Get product being modified
        Product productToModify = Inventory.getAllProducts().get(indexOfProduct);
        //Sets data
        productId = productToModify.getId();
        modifyProductIdTF.setText(String.valueOf(productId));
        modifyProductIdTF.setDisable(true);
        modifyProductNameTF.setText(productToModify.getName());
        modifyProductInvTF.setText(Integer.toString(productToModify.getStock()));
        modifyProductPriceTF.setText(Double.toString(productToModify.getPrice()));
        modifyProductMaxTF.setText(Integer.toString(productToModify.getMax()));
        modifyProductMinTF.setText(Integer.toString(productToModify.getMin()));
    }

    /**
     * Populates associated parts table
     */
    private void populateAddedPartsTable() {
        //Gets associated parts from product
        addedPartsTable.setItems(productToModify().getAssociatedParts());

        addedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Removes added part from product and the table
     */
    public void removeAddedParts() {
        //Gets part being removed
        partToRemove = addedPartsTable.getSelectionModel().getSelectedItem();
        if(partToRemove == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No part was selected to remove");
            alert.setContentText("Please select a part to remove from the product");
            alert.showAndWait().ifPresent(response -> {

            });
        } else {
            try {
                //Setting alert box for part association removal
                ButtonType deleteButton = new ButtonType("Delete");
                ButtonType cancelButton = new ButtonType("Cancel");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete part association", deleteButton, cancelButton);
                alert.setContentText("Are you sure you want to remove the part association?");
                alert.setHeaderText("Delete part association");
                alert.showAndWait().ifPresent(response -> {
                    if (response == deleteButton) {
                        if (productToModify().deleteAssociatedPart(partToRemove)) {
                            System.err.println("successfully deleted association");
                        } else {
                            System.err.println("Association was not deleted");
                        }
                    } else if (response == cancelButton) {
                        alert.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("Please select a part to remove association");
            }
        }
    }

    /**
     * Adds part to product and the association table view
     */
    public void addParts() {
        //Gets the part being added to the product
        partToAdd = availablePartTableView.getSelectionModel().getSelectedItem();

        if(partToAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No part was selected to add");
            alert.setContentText("Please select a part to add to the product");
            alert.showAndWait().ifPresent(response -> {

            });
        } else {
            //Adds to product
            productToModify().addAssociatePart(partToAdd);
            populateAddedPartsTable();
        }

    }

    /**
     * Checks if a new product was created or if no differences were present. Returns to main screen
     * @param actionEvent Save button pressed
     */
    public void saveModifiedProduct(ActionEvent actionEvent) {
        //Checks if differences are present; if not it checks for errors then tries to create new product
        if(checkDifferences()) {
            cancelModifyProduct(actionEvent);
        } else if(!checkDifferences()) {
            checkAllFields();
            presentErrors();
            if (checkIfInvValid()) {
                if (createProduct()) {
                    cancelModifyProduct(actionEvent);
                }
            }
            presentErrors();
        }
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

    /**
     * Checks if any differences are present between text fields and data in inventory
     * @return True if no differences are present
     */
    private boolean checkDifferences() {
        if(checkProductNameDiff() && checkProductPriceDiff()  && checkProductInvDiff()
                && checkProductMinDiff() && checkProductMax()) {
            System.out.println("No differences");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Changes the color of the text fields that has invalid data
     */
    public void presentErrors() {
        if(!maxCheck) {
            modifyProductMaxTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyProductMaxTF.setStyle("-fx-border-color: #9f07");
        }

        if(!minCheck) {
            modifyProductMinTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyProductMinTF.setStyle("-fx-border-color: #9f07");
        }
        if(!invCheck) {
            modifyProductInvTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyProductInvTF.setStyle("-fx-border-color: #9f07");
        }

        if(!priceCheck) {
            modifyProductPriceTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyProductPriceTF.setStyle("-fx-border-color: #9f07");
        }
        if(!nameCheck) {
            modifyProductNameTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyProductNameTF.setStyle("-fx-border-color: #9f07");
        }
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

    /**
     * Checks name field to make sure valid input data is provided
     */
    private void checkNameField() {
        String input = modifyProductNameTF.getText();
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars) {
            if(Character.isDigit(c)) {
                sb.append(c);
            }
        }
        if(sb.length() == 0) {
            if(input.length() == 0 || input.matches("[0-9]*")) {
                System.err.println("Provide a name please");
                nameCheck = false;
            } else {
                System.out.println(sb);
                nameCheck = true;
            }
        } else {
            nameCheck = false;
        }

    }

    /**
     * Checks price field to see if invalid data is given
     */
    private void checkPriceField() {
        String input = modifyProductPriceTF.getText();
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
        }catch(Exception e) {
            System.err.println("Please provide a number for price");
            priceCheck = false;
        }
    }

    /**
     * Checks inventory field to see if invalid data is given
     */
    private void checkInvField() {
        String input = modifyProductInvTF.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            invCheck = true;
        }catch (Exception e) {
            System.err.println("Please provide a number for inventory");
            invCheck = false;
        }
    }

    /**
     * Checks min field to see if invalid data is given
     */
    private void checkMinField() {
        String input = modifyProductMinTF.getText();
        int tryInt;
        try {
            tryInt = Integer.parseInt(input.trim());
            minCheck = true;
        }catch(Exception e) {
            System.err.println("Please provide a number for min");
            minCheck = false;
        }
    }

    /**
     * Checks max field to see if invalid data is given
     */
    private void checkMaxField() {
        String input = modifyProductMaxTF.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            maxCheck = true;
        }catch(Exception e) {
            maxCheck = false;
            System.err.println("Please provide a number for max");
        }
    }

    /**
     * Checks product min text field and inventory to see if differences are present
     * @return True if no differences are present
     */
    private boolean checkProductMinDiff() {
        String input = modifyProductMinTF.getText();
        boolean isTheSame = false;
        int tryInt;
        try {
            tryInt = Integer.parseInt(input.trim());
            minCheck = true;
            isTheSame = tryInt == productToModify().getMin();
        }catch(Exception e) {
            System.err.println("Please provide a number for min");
            minCheck = false;
        }
        return isTheSame;
    }

    /**
     * Checks product max text field and inventory to see if differences are present
     * @return True if no differences are present
     */
    private boolean checkProductMax() {
        String input = modifyProductMaxTF.getText();
        boolean isTheSame = false;
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            maxCheck = true;
            isTheSame = tryInt == productToModify().getMax();
        }catch(Exception e) {
            maxCheck = false;
            System.err.println("Please provide a number for max");
        }

        return isTheSame;
    }

    /**
     * Checks product price text field and inventory to see if differences are present
     * @return True if no differences are present
     */
    private boolean checkProductPriceDiff() {
        String input = modifyProductPriceTF.getText();
        boolean isTheSame = false;
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
            isTheSame = tryDouble == productToModify().getPrice();
        }catch(Exception e) {
            System.err.println("Please provide a number for price");
            priceCheck = false;
        }
        return isTheSame;
    }

    /**
     * Checks product inv text field and inventory to see if differences are present
     * @return True if no differences are present
     */
    private boolean checkProductInvDiff() {
        String input = modifyProductInvTF.getText();
        boolean isTheSame = false;
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            invCheck = true;
            isTheSame = tryInt == productToModify().getStock();
        }catch (Exception e) {
            System.err.println("Please provide a number for inventory");
            invCheck = false;
        }
        return isTheSame;
    }

    /**
     * Checks product name fields and inventory for differences
     * @return True if no differences
     */
    private boolean checkProductNameDiff() {
        return modifyProductNameTF.getText().equals(Inventory.getAllProducts().get(productIndexToModify()).getName());
    }

    /**
     * Tries to create new product with given data after checking fields
     * @return True if product was created and added to inventory successfully
     */
    public boolean createProduct() {
        boolean created = false;
        if(checkProductNameDiff() && checkProductInvDiff() && checkProductPriceDiff() && checkProductMax()
                && checkProductMinDiff()) {
            System.out.println("No differences");
        } else {
            try {
                String productName = modifyProductNameTF.getText();
                char[] chars = productName.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (char c : chars) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                if(sb.length() == 0) {
                    int productStock = Integer.parseInt(modifyProductInvTF.getText());
                    double productPrice = Double.parseDouble(modifyProductPriceTF.getText());
                    int productMax = Integer.parseInt(modifyProductMaxTF.getText());
                    int productMin = Integer.parseInt(modifyProductMinTF.getText());

                    Product newProduct = new Product(productToModify().getId(), productName, productPrice, productStock, productMin, productMax);
                    for (Part part : productToModify().getAssociatedParts()) {
                        newProduct.addAssociatePart(part);
                    }
                    Inventory.updateProduct(productIndexToModify(), newProduct);
                    System.out.println("Product updated successfully");
                    created = true;
                    setAllChecksToFalse();
                } else {
                    created = false;
                    setAllChecksToFalse();
                }
            }catch(Exception ex) {
                presentErrors();
                System.err.println("Please provide numbers only");
            }
        }
        return created;
    }

    /**
     * Displays an alert box for no search results found for parts
     *
     */
    private void noResultAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("No results found for part");
        alert.setContentText("No results were found for this search");
        alert.showAndWait().ifPresent(response -> {
        });
    }

    /**
     * Searches part inventory for an ID given by user in search box
     * @param query Given by user
     * @return List of parts that match query
     */
    public ObservableList<Part> searchPartById(String query) {
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        for(Part part : Inventory.getAllParts()) {
            if((part.getId() == Integer.parseInt(query))) {
                searchPart.add(part);
            }
        }
        if(searchPart.size() == 0 ) {
            System.err.println("No ID matches for part");
        }
        return searchPart;
    }

    /**
     * Searches part inventory for a part name given by user in search box
     * @param query Given by user
     * @return List of parts that match query
     */
    public ObservableList<Part> searchPartByName(String query) {
        ObservableList<Part> searchPart = FXCollections.observableArrayList();
        for(Part part : Inventory.getAllParts()) {
            if(part.getName().toLowerCase().contains(query.toLowerCase())) {
                searchPart.add(part);
            }
        }
        if(searchPart.size() == 0 ) {
            System.err.println("No results for part name");
        }
        return searchPart;
    }

    /**
     * Main method to search for part. Combines ID and name search together and sets TableView to results
     */
    public void searchPart() {
        String query = modifyProductPartTF.getText();
        ObservableList<Part> idList;
        if(query.matches("[0-9]*") && query.length() != 0) {
            idList = searchPartById(query);
            if(idList.size() == 0) {
                noResultAlert();
                updatePartsTable();
            } else {
                availablePartTableView.setItems(idList);
            }
        } else {
            ObservableList<Part> tmpList = searchPartByName(query);
            if(tmpList.size() == 0) {
                noResultAlert();
                updatePartsTable();
            } else {
                availablePartTableView.setItems(tmpList);
            }
        }
    }

    /**
     * Updates the parts table with current inventory of parts
     */
    public void updatePartsTable() {
        availablePartTableView.setItems(Inventory.getAllParts());
    }

    /**
     * Checks if inventory data is logically valid
     * @return True if inventory data is logically valid
     */
    private boolean checkIfInvValid() {
        boolean isValid = false;
        try {
            int min = Integer.parseInt(modifyProductMinTF.getText());
            int max = Integer.parseInt(modifyProductMaxTF.getText());
            int inv = Integer.parseInt(modifyProductInvTF.getText());

            if(min > max) {
                minCheck = false;
                maxCheck = false;
                isValid = false;
            }

            if(inv < min) {
                invCheck = false;
                minCheck = false;
                isValid = false;
            }

            if(inv > max) {
                invCheck = false;
                maxCheck = false;
                isValid = false;
            }

            if((inv <= max) && (inv >= min)) {
                invCheck = true;
                maxCheck = true;
                minCheck = true;
                isValid = true;
            }
        }catch (Exception e) {
            System.err.println("Numbers only for inv data");
        }
        if(!maxCheck || !minCheck || !invCheck) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Numbers only for inv, min, and max");
            alert.setContentText("Please only include numbers for inventory, min, and max");
            alert.showAndWait().ifPresent(response -> {

            });
        }
        return isValid;
    }
}
