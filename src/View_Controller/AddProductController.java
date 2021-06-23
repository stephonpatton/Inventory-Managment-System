package View_Controller;

import Model.Inventory;
import Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    private int productID;
    @FXML private TextField addProductIdTF;
    @FXML private TextField addProductName;
    @FXML private TextField addProductInv;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductMax;
    @FXML private TextField addProductMin;

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

    public void createProduct() {
        String productName = addProductName.getText();
        int productStock = Integer.parseInt(addProductInv.getText());
        double productPrice = Double.parseDouble(addProductPrice.getText());
        int productMin = Integer.parseInt(addProductMin.getText());
        int productMax = Integer.parseInt(addProductMax.getText());

        if(checkAddProductName() && checkAddProductInv() && checkAddProductPrice()
        && checkAddProductMax() && checkAddProductMin()) {
            Product newProduct = new Product(Inventory.getProductIDCount(), productName, productPrice, productStock, productMax, productMin);
            Inventory.addProduct(newProduct);
        }
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
        if(addProductMax.getText().matches("[0-9\\.]*") && addProductMax.getLength() != 0) {
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
    }

    public void addProductSubmit(ActionEvent actionEvent) {
        createProduct();
        cancelAddProduct(actionEvent);
    }
}
