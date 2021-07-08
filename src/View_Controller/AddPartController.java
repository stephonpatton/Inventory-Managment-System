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
    //Holds the partID from inventory (next number for Add)
    private int partID;

    //Inhouse Radio button
    @FXML private RadioButton sourceButton;
    //Outsourced Radio button
    @FXML private RadioButton addPartOutsourcedButton;
    //Label for company name/machine ID
    @FXML private Label invSourceLabel;
    //Text Field for part name
    @FXML private TextField addPartNameTF;
    //Text field for part inventory
    @FXML private TextField addPartInvTF;
    //Text field for part price
    @FXML private TextField addPartPriceTF;
    //Text field for part max
    @FXML private TextField addPartMaxTF;
    //Text field for part min
    @FXML private TextField addPartMinTF;
    //Text field for company name/machine ID
    @FXML private TextField addPartMachineIDCompNameTF;
    //Text field for ID
    @FXML private TextField addPartIDTextField;

    /**
     * Checks which radio button is selected and updates label
     */
    public void checkRadioButton() {
        if(sourceButton.isSelected()) {
            invSourceLabel.setText("Machine ID");
        } else {
            invSourceLabel.setText("Company Name");
        }
    }

    /**
     * Returns to main screen after cancel is pressed on Add Part screen
     * @param actionEvent cancel button pressed
     */
    public void cancelAddPart(ActionEvent actionEvent) {
        Parent root;
        try {
            //Sets the main screen and adds properties
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

    /**
     * Checks if name field has data and the data does not contain numbers
     * @return True if valid data is provided
     */
    public boolean checkAddPartName() {
        if(addPartNameTF.getLength() == 0 || addPartNameTF.getText().matches("[0-9]*")) {
            //TODO: Add alert box or error label box
            System.out.println("Please provide a name");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if inventory field has data and the data is numbers only
     * @return True if input data is valid
     */
    public boolean checkAddPartInv() {
        if(addPartInvTF.getText().matches("[0-9]*") && addPartInvTF.getLength() != 0) {
            return true;
        } else {
            //TODO: Alert box
            System.out.println("Please use numbers only");
            return false;
        }

    }

    /**
     * Checks if price field has data and the data is valid (numbers only)
     * @return True if input data is valid
     */
    public boolean checkAddPartPrice() {
        if((addPartPriceTF.getText().matches("[0-9]*") || Double.parseDouble(addPartPriceTF.getText()) % 1 != 0) && addPartPriceTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide numbers for price.");
            //TODO: Alert box
            return false;
        }
    }

    /**
     * Checks if max field has data and the data is valid (numbers only)
     * @return True if input data is valid
     */
    public boolean checkAddPartMax() {
        if(addPartMaxTF.getText().matches("[0-9]*") && addPartMaxTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for max");
            return false;
        }
    }

    /**
     * Checks if min field has data and that the data is valid
     * @return True if input data is valid
     */
    public boolean checkAddPartMin() {
        if(addPartMinTF.getText().matches("[0-9]*") && addPartMinTF.getLength() != 0) {
            return true;
        } else {
            System.out.println("Please provide a number for min");
            //TODO: Alert box
            return false;
        }
    }

    /**
     * Checks which radio button is selected (or if any)
     * @return True if a button is selected
     */
    public boolean checkAddPartSource() {
        if(sourceButton.isSelected()) {
            return checkAddPartMachineID();
        } else if(addPartOutsourcedButton.isSelected()) {
            return checkAddPartCompName();
        } else {
            System.out.println("Please select a part source.");
            //TODO: Alert box
            return false;
        }
    }

    /**
     * Checks to make sure company name field is not empty and doesn't contain numbers
     * @return True if data is valid
     */
    public boolean checkAddPartCompName() {
        if(addPartMachineIDCompNameTF.getLength() != 0 && !addPartMachineIDCompNameTF.getText().matches("[0-9]*")) {
            return true;
        } else {
            //TODO: Alert box
            System.out.println("Please provide a company name");
            return false;
        }
    }

    /**
     * Checks to make sure machine ID field has data and that the data is numbers only
     * @return True if data is valid
     */
    public boolean checkAddPartMachineID() {
        if(addPartMachineIDCompNameTF.getText().matches("[0-9]*") && addPartMachineIDCompNameTF.getLength() != 0) {
            return true;
        } else {
            //TODO: Alert box
            System.out.println("Please provide numbers only for Machine ID");
            return false;
        }
    }

    /**
     * Checks to make sure data is valid and then calls createPart to save data and closes current window
     * @param actionEvent When the save button is pressed
     */
    public void addPartSubmit(ActionEvent actionEvent) {
        boolean success = false;
        if(checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
        && checkAddPartMin() && checkAddPartSource()) {
            success = createPart();
        }
        if(success) {
            cancelAddPart(actionEvent);
        } else {
            ButtonType okayButton = new ButtonType("Okay");
            ButtonType cancelButton = new ButtonType("Cancel");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part not created", okayButton, cancelButton);
            alert.showAndWait().ifPresent(response -> {
                if(response == okayButton) {
                    alert.close();
                } else if(response == cancelButton) {
                    alert.close();
                }
            });
        }
    }

    /**
     * Creates the part object after all data is submitted and adds it to the ObservableList
     */
    public boolean createPart() {
        boolean success = false;
        String partName = addPartNameTF.getText();
        double partPrice = Double.parseDouble(addPartPriceTF.getText());
        int partStock = Integer.parseInt(addPartInvTF.getText());
        int partMin = Integer.parseInt(addPartMinTF.getText());
        int partMax = Integer.parseInt(addPartMaxTF.getText());
        String partMachineId = addPartMachineIDCompNameTF.getText();

        if(checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
                && checkAddPartMin() && checkAddPartSource()) {
            if ((partStock > partMin) & (partStock < partMax)) {
                if (sourceButton.isSelected()) {
                    InhousePart newInhousePart = new InhousePart(partID, partName, partPrice, partStock, partMin, partMax,
                            Integer.parseInt(partMachineId));
                    Inventory.addPart(newInhousePart);
                    success = true;
                } else if (addPartOutsourcedButton.isSelected()) {
                    OutsourcedPart outsourcedPart = new OutsourcedPart(partID, partName, partPrice, partStock, partMin, partMax, partMachineId);
                    Inventory.addPart(outsourcedPart);
                    success = true;
                }
            } else {
                ButtonType okayButton = new ButtonType("Okay");
                ButtonType cancelButton = new ButtonType("Cancel");
                Alert alert = new Alert(Alert.AlertType.ERROR, "", okayButton, cancelButton);
                alert.setContentText("Min < Max and Inventory in between min and max");
                alert.setHeaderText("Please fix min, max, and inventory values");
                alert.showAndWait().ifPresent(response -> {
                    if(response == okayButton) {
                        alert.close();
                    } else if(response ==cancelButton) {
                        alert.close();
                    }
                });
            }
        }
        return success;
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
