package View_Controller;
import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    private int partID;

    @FXML private RadioButton sourceButton;
    @FXML private Label invSourceLabel;
    @FXML private TextField addPartNameTF;
    @FXML private TextField addPartInvTF;
    @FXML private TextField addPartPriceTF;
    @FXML private TextField addPartMaxTF;
    @FXML private TextField addPartMinTF;
    @FXML private TextField addPartMachineIDCompNameTF;
    @FXML private RadioButton addPartOutsourcedButton;
    @FXML private TextField addPartIDTextField;

    public void checkRadioButton() {
        if(sourceButton.isSelected()) {
            invSourceLabel.setText("Machine ID");
        } else {
            invSourceLabel.setText("Company Name");
        }
    }

    public void cancelAddPart(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View_Controller/mainform.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Main Page");
            stage.setScene(new Scene(root, 1100, 500));
            stage.setResizable(false);
            stage.show();

            //Hides current window/scene
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAddPartName() {
        if(addPartNameTF.getLength() == 0 || addPartNameTF.getText().matches("[0-9]*")) {
            System.out.println("Please provide a name");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkAddPartInv() {
        if(addPartInvTF.getText().matches("[0-9]*") && addPartInvTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please use numbers only");
            return false;
        }

    }

    public boolean checkAddPartPrice() {
        if((addPartPriceTF.getText().matches("[0-9]*") || Double.parseDouble(addPartPriceTF.getText()) % 1 != 0) && addPartPriceTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide numbers for price.");
            return false;
        }
    }

    public boolean checkAddPartMax() {
        if(addPartMaxTF.getText().matches("[0-9]*") && addPartMaxTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for max");
            return false;
        }
    }

    public boolean checkAddPartMin() {
        if(addPartMinTF.getText().matches("[0-9]*") && addPartMinTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for min");
            return false;
        }
    }

    public boolean checkAddPartSource() {
        if(sourceButton.isSelected()) {
            return checkAddPartMachineID();
        } else if(addPartOutsourcedButton.isSelected()) {
            return checkAddPartCompName();
        } else {
            System.out.println("Please select a part source.");
            return false;
        }
    }

    public boolean checkAddPartCompName() {
        if(addPartMachineIDCompNameTF.getLength() != 0 && !addPartMachineIDCompNameTF.getText().matches("[0-9]*")) {
            return true;
        } else {
            System.out.println("Please provide a company name");
            return false;
        }
    }

    public boolean checkAddPartMachineID() {
        if(addPartMachineIDCompNameTF.getText().matches("[0-9]*") && addPartMachineIDCompNameTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide numbers only for Machine ID");
            return false;
        }
    }

    /**
     * Checks to make sure data is valid and then calls createPart to save data and closes current window
     * @param actionEvent when the save button is pressed
     */
    public void addPartSubmit(ActionEvent actionEvent) {
        if(checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
        && checkAddPartMin() && checkAddPartSource()) {
            createPart();
           cancelAddPart(actionEvent);
        }
    }

    /**
     * Creates the part object after all data is submitted and adds it to the ObservableList
     */
    public void createPart() {
        String partName = addPartNameTF.getText();
        double partPrice = Double.parseDouble(addPartPriceTF.getText());
        int partStock = Integer.parseInt(addPartInvTF.getText());
        int partMin = Integer.parseInt(addPartMinTF.getText());
        int partMax = Integer.parseInt(addPartMaxTF.getText());
        String partMachineId = addPartMachineIDCompNameTF.getText();

        if(checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
                && checkAddPartMin() && checkAddPartSource()) {
            if(sourceButton.isSelected()) {
                InhousePart newInhousePart = new InhousePart(partID, partName, partPrice, partStock, partMin, partMax,
                        Integer.parseInt(partMachineId));
                Inventory.addPart(newInhousePart);
            }
            else if(addPartOutsourcedButton.isSelected()) {
                OutsourcedPart outsourcedPart = new OutsourcedPart(partID, partName, partPrice, partStock, partMin, partMax, partMachineId);
                Inventory.addPart(outsourcedPart);
            }
        }
    }

    /**
     * Initializes the controller upon loading of the AddPart screen
     * @param url required with method
     * @param rb required with method
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        addPartIDTextField.setText("AUTO GEN: " + partID);
    }


}
