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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static View_Controller.MainController.productIndexToModify;
import static View_Controller.MainController.productToModify;

public class ModifyProductController implements Initializable {
    private int productId;

    @FXML private TextField modifyProductIdTF;
    @FXML private TextField modifyProductNameTF;
    @FXML private TextField modifyProductInvTF;
    @FXML private TextField modifyProductPriceTF;
    @FXML private TextField modifyProductMaxTF;
    @FXML private TextField modifyProductMinTF;

    @FXML private TableView<Part> availablePartTableView;
    @FXML private TableColumn<Part, Integer> availablePartsPartIdCol;
    @FXML private TableColumn<Part, String> availablePartsNameCol;
    @FXML private TableColumn<Part, Integer> availablePartsInvCol;
    @FXML private TableColumn<Part, Double> availablePartsPriceCol;

    @FXML private TableView<Part> addedPartsTable;
    @FXML private TableColumn<Part, Integer> addedPartsId;
    @FXML private TableColumn<Part, String> addedPartsName;
    @FXML private TableColumn<Part, Integer> addedPartsInv;
    @FXML private TableColumn<Part, Double> addedPartsPrice;

    private Part partToRemove;
    private Part partToAdd;
    ObservableList<Part> tempList = FXCollections.observableArrayList();
    private int tempPartIndex;

    int indexOfProduct = productIndexToModify();

    public void cancelModifyProduct(ActionEvent actionEvent)  {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/mainform.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Main Page");
            stage.setScene(new Scene(root, 1100, 500));
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateProductTable() {
        availablePartTableView.setItems(Inventory.getAllParts());

        availablePartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateFields();
        } catch(Exception e) {
            System.err.println("No data to populate fields");
        }
        try {
            populateProductTable();
            populateAddedPartsTable();
        }catch(Exception e) {
            System.err.println("No data to populate data");
        }
    }

    public void populateFields() {
        Product productToModify = Inventory.getAllProducts().get(indexOfProduct);
        productId = productToModify.getId();
        modifyProductIdTF.setText(String.valueOf(productId));
        modifyProductIdTF.setDisable(true);
        modifyProductNameTF.setText(productToModify.getName());
        modifyProductInvTF.setText(Integer.toString(productToModify.getStock()));
        modifyProductPriceTF.setText(Double.toString(productToModify.getPrice()));
        modifyProductMaxTF.setText(Integer.toString(productToModify.getMax()));
        modifyProductMinTF.setText(Integer.toString(productToModify.getMin()));

    }

    private void populateAddedPartsTable() {
        addedPartsTable.setItems(productToModify().getAssociatedParts());

        addedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void removeAddedParts() {
        try {
            partToRemove = addedPartsTable.getSelectionModel().getSelectedItem();
            if (productToModify().deleteAssociatedPart(partToRemove)) {
                System.out.println("successfully deleted association");
            } else {
                System.out.println("Association was not deleted");
            }
        }catch(Exception e) {
            System.err.println("Please select a part to remove association");
        }
    }

    public void addParts() {
        partToAdd = availablePartTableView.getSelectionModel().getSelectedItem();

        productToModify().addAssociatePart(partToAdd);
        populateAddedPartsTable();
    }

    public void saveModifiedProduct(ActionEvent actionEvent) {
        if(!checkDifferences()) {
            System.out.println("Created product");
        }
        cancelModifyProduct(actionEvent);
    }

    private boolean checkDifferences() {
        if(checkProductNameDiff() && checkProductInvDiff() && checkProductPriceDiff() && checkProductMax()
        && checkProductMinDiff()) {
            System.out.println("No differences");
            return true;
        } else {
            createProduct();
            return false;
        }
    }

    public boolean checkProductMinDiff() {
        if(modifyProductMinTF.getText().matches("[0-9]*") && modifyProductMinTF.getLength() != 0) {
            return Integer.parseInt(modifyProductMinTF.getText()) == productToModify().getMin();
        } else {
            System.out.println("ENTER SOMETHING");
            return false;
        }
    }

    private boolean checkProductMax() {
        int productMax = Integer.parseInt(modifyProductMaxTF.getText());
        return productMax == Inventory.getAllProducts().get(productIndexToModify()).getMax();
    }

    private boolean checkProductPriceDiff() {
        double price = Double.parseDouble(modifyProductPriceTF.getText());
        return price == Inventory.getAllProducts().get(productIndexToModify()).getPrice();
    }

    private boolean checkProductInvDiff() {
        int productStock = Integer.parseInt(modifyProductInvTF.getText());
        return productStock == Inventory.getAllProducts().get(productIndexToModify()).getStock();
    }

    private boolean checkProductNameDiff() {
        return modifyProductNameTF.getText().equals(Inventory.getAllProducts().get(productIndexToModify()).getName());
    }

    public void createProduct() {
        String productName = modifyProductNameTF.getText();
        int productStock = Integer.parseInt(modifyProductInvTF.getText());
        double productPrice = Double.parseDouble(modifyProductPriceTF.getText());
        int productMax = Integer.parseInt(modifyProductMaxTF.getText());
        int productMin = Integer.parseInt(modifyProductMinTF.getText());

        Product newProduct = new Product(productToModify().getId(), productName, productPrice, productStock, productMin, productMax);
        for(Part part : productToModify().getAssociatedParts()) {
            newProduct.addAssociatePart(part);
        }
        Inventory.updateProduct(productIndexToModify(), newProduct);
        System.out.println("Product updated successfully");
    }

    //TODO: Add search part functionality
}
