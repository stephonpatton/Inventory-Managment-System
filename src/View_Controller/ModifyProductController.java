package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
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

    int indexOfProduct = productIndexToModify();

    //TODO: Implemented modify product (bring data from selected product)
    //TODO: Must populate table with associated parts as well
    //TODO: Can delete associated parts and also add more

    public void cancelModifyPart(ActionEvent actionEvent)  {
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

    //TODO: Remove associated part when remove is pressed

}
