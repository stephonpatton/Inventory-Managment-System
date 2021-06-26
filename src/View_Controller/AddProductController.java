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

import static View_Controller.MainController.productToModify;

public class AddProductController implements Initializable {
    private int productID;
    @FXML private TextField addProductIdTF;
    @FXML private TextField addProductName;
    @FXML private TextField addProductInv;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductMax;
    @FXML private TextField addProductMin;

    @FXML private TableView<Part> availablePartTableView;
    @FXML private TableColumn<Part, Integer> availablePartsPartIdCol;
    @FXML private TableColumn<Part, String> availablePartsNameCol;
    @FXML private TableColumn<Part, Integer> availablePartsInvCol;
    @FXML private TableColumn<Part, Double> availablePartsPriceCol;

    @FXML private TableView<Part> addProductAddedPartsTableView;
    @FXML private TableColumn<Part, Integer> addedPartsId;
    @FXML private TableColumn<Part, String> addedPartsName;
    @FXML private TableColumn<Part, Integer> addedPartsInv;
    @FXML private TableColumn<Part, Double> addedPartsPrice;

    private Part partToAdd;
    private Part partToRemove;
    ObservableList<Part> tempList = FXCollections.observableArrayList();
    ObservableList<Part> tempRemoveList = FXCollections.observableArrayList();

    public void cancelAddProduct(ActionEvent actionEvent) {
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

    public Product createProduct() {
        Product newProduct;
        String productName = addProductName.getText();
        int productStock = Integer.parseInt(addProductInv.getText());
        double productPrice = Double.parseDouble(addProductPrice.getText());
        int productMin = Integer.parseInt(addProductMin.getText());
        int productMax = Integer.parseInt(addProductMax.getText());
        newProduct = new Product(Inventory.getProductIDCount(), productName, productPrice, productStock, productMax, productMin);
        Inventory.addProduct(newProduct);
        return newProduct;
    }

    public boolean checkAddProductPrice() {
        if((addProductPrice.getText().matches("[0-9]*") || Double.parseDouble(addProductPrice.getText()) % 1 != 0) && addProductPrice.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide numbers for the price.");
            return false;
        }
    }

    public boolean checkAddProductMin() {
        if(addProductMin.getText().matches("[0-9]*") && addProductMin.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for min");
            return false;
        }
    }

    public boolean checkAddProductMax() {
        if(addProductMax.getText().matches("[0-9]*") && addProductMax.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for max");
            return false;
        }
    }

    public boolean checkAddProductInv() {
        if(addProductInv.getText().matches("[0-9]*") && addProductInv.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please use numbers only");
            return false;
        }
    }

    public boolean checkAddProductName() {
        if(addProductName.getLength() == 0 || addProductName.getText().matches("[0-9]*")) {
            System.out.println("Please provide a name");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productID = Inventory.getProductIDCount();
        addProductIdTF.setText("AUTO GEN: " + productID);
        try {
            populateProductTable();
        }catch(Exception e) {
            System.err.println("No data to populate data");
        }
    }

    public void populateProductTable() {
        availablePartTableView.setItems(Inventory.getAllParts());

        availablePartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    public void addProductSubmit(ActionEvent actionEvent) {
        if(checkAddProductName() && checkAddProductInv() && checkAddProductPrice()
                && checkAddProductMax() && checkAddProductMin()) {
            Product product = createProduct();
            addAllPartsToProduct(tempList,product);
            cancelAddProduct(actionEvent);
        }
    }

    private void addAllPartsToProduct(ObservableList<Part> list, Product product) {
        for(Part part : list) {
            product.addAssociatePart(part);
        }
    }

    public void addPartToProduct() {
        partToAdd = availablePartTableView.getSelectionModel().getSelectedItem();
        tempList.add(partToAdd);

        addProductAddedPartsTableView.setItems(tempList);
        addedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void removeAddedParts() {
        partToRemove = addProductAddedPartsTableView.getSelectionModel().getSelectedItem();
        tempList.remove(partToRemove);
    }

    //TODO: Add search part functionality
}
