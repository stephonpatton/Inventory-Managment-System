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

    //Boolean variables to check if fields have valid data (used for highlighting fields)
    private boolean minCheck;
    private boolean maxCheck;
    private boolean priceCheck;
    private boolean invCheck;
    private boolean nameCheck;
    private boolean machineIdCheck;
    private boolean companyNameCheck;


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
            nameCheck = false;
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
            invCheck = true;
            return true;
        } else {
            invCheck = false;
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
            priceCheck = true;
            return true;
        } else {
            System.out.println("Please provide numbers for price.");
            priceCheck = false;
            return false;
        }
    }

    /**
     * Checks if max field has data and the data is valid (numbers only)
     * @return True if input data is valid
     */
    public boolean checkAddPartMax() {
        if(addPartMaxTF.getText().matches("[0-9]*") && addPartMaxTF.getLength() != 0) {
            maxCheck = true;
            return true;
        } else {
            maxCheck = false;
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
            minCheck = true;
            return true;
        } else {
            minCheck = false;
            System.out.println("Please provide a number for min");
            return false;
        }
    }

    /**
     * Checks which radio button is selected (or if any)
     * @return True if a button is selected
     */
    public boolean checkAddPartSource() {
        if(sourceButton.isSelected()) {
            System.out.println("INHOUSE SELECTED");
            return checkAddPartMachineID();
        } else if(addPartOutsourcedButton.isSelected()) {
            System.out.println("OUTSOURCED SELECTED");
            return checkAddPartCompName();
        } else {
            System.out.println("Please select a part source.");
            return false;
        }
    }

    /**
     * Checks to make sure company name field is not empty and doesn't contain numbers
     * @return True if data is valid
     */
    public boolean checkAddPartCompName() {
        if(addPartMachineIDCompNameTF.getLength() != 0 && !addPartMachineIDCompNameTF.getText().matches("[0-9]*")) {
            companyNameCheck = true;
            System.out.println("COMPANY NAME TRUE");
            return true;
        } else {
            companyNameCheck = false;
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
            machineIdCheck = true;
            System.out.println("MACHINE ID TRUE");
            return true;
        } else {
            machineIdCheck = false;
            System.out.println("Please provide numbers only for Machine ID");
            return false;
        }
    }

    /**
     * Sets field checking values to false to reset error checking for field highlighting
     */
    private void setAllChecksToFalse() {
        maxCheck = false;
        minCheck = false;
        priceCheck = false;
        invCheck = false;
        machineIdCheck = false;
        companyNameCheck = false;
        nameCheck = false;
    }

    /**
     * Checks all fields for valid data and sets fields to true or false for highlighting
     */
    private void checkAllFields() {
        checkPriceField();
        checkInvField();
        checkMaxField();
        checkMinFields();
        checkMachineIdFields();
        checkNameField();
    }

    /**
     * Checks name field for errors and sets check to true or false for highlighting
     */
    private void checkNameField() {
        String input = addPartNameTF.getText();
        nameCheck = !input.matches(".*\\d.*") && input.length() != 0;
    }

    /**
     * Checks min field for errors and sets check to true or false for highlighting
     */
    private void checkMinFields() {
        String input = addPartMinTF.getText();
        int tryInt;
        try {
            tryInt = Integer.parseInt(input.trim());
            minCheck = true;
        }catch(Exception e) {
            minCheck = false;
        }
    }

    /**
     * Checks max field for errors and sets check to true or false for highlighting
     */
    private void checkMaxField() {
        String input = addPartMaxTF.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            maxCheck = true;
        }catch(Exception e) {
            maxCheck = false;
        }
    }

    /**
     * Checks inv field for errors and sets check to true or false for highlighting
     */
    private void checkInvField() {
        String input = addPartInvTF.getText();
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            invCheck = true;
        }catch (Exception e) {
            invCheck = false;
        }
    }

    /**
     * Checks price field for errors and sets check to true or false for highlighting
     */
    private void checkPriceField() {
        String input = addPartPriceTF.getText();
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
        }catch(Exception e) {
            priceCheck = false;
        }
    }

    /**
     * Checks machine ID/company name field for errors and sets check to true or false for highlighting
     */
    private void checkMachineIdFields() {
        if(sourceButton.isSelected()) {
            String input = addPartMachineIDCompNameTF.getText();
            int tryInt = 0;
            try {
                machineIdCheck = true;
                tryInt = Integer.parseInt(input.trim());
            } catch (Exception e) {
                machineIdCheck = false;
            }
        } else {
            String input = addPartMachineIDCompNameTF.getText();
            companyNameCheck = !input.matches("[0-9]*") && input.length() != 0;
        }
    }

    /**
     * Checks to make sure data is valid and then calls createPart to save data and closes current window
     * @param actionEvent When the save button is pressed
     */
    public void addPartSubmit(ActionEvent actionEvent) {
        checkAllFields();
        checkIfInvValid();
        presentErrors();
        if(createPart()) {
            cancelAddPart(actionEvent);
        } else {
            setAllChecksToFalse();
        }
    }

    /**
     * Logical error checking for min, max, and inv. Min < Inv < Max
     */
    private void checkIfInvValid() {
        try {
            int min = Integer.parseInt(addPartMinTF.getText());
            int max = Integer.parseInt(addPartMaxTF.getText());
            int inv = Integer.parseInt(addPartInvTF.getText());

            if (min > max) {
                minCheck = false;
                maxCheck = false;
            }

            if (inv < min) {
                invCheck = false;
                minCheck = false;
            }

            if (inv > max) {
                invCheck = false;
                maxCheck = false;
            }

            if(min == max) {
                minCheck = false;
                maxCheck = false;
            }

            if ((inv <= max) && (inv >= min) && min != max) {
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
    }


    /*
     * "RUNTIME ERROR"
     * NumberFormatException when trying to convert string type into an int for part creation.
     * Had to encapsulate the code in a try-catch in order to catch the error if invalid data is provided.
     */
    /**
     * Creates the part object after all data is submitted and adds it to the ObservableList
     */
    public boolean createPart() {
        boolean success = false;
        //Attempts to create part based on information given
        try {
            String partName = addPartNameTF.getText();
            double partPrice = Double.parseDouble(addPartPriceTF.getText());
            int partStock = Integer.parseInt(addPartInvTF.getText());
            int partMin = Integer.parseInt(addPartMinTF.getText());
            int partMax = Integer.parseInt(addPartMaxTF.getText());
            String partMachineId = addPartMachineIDCompNameTF.getText();
            //Double checking to make sure name is valid
            if(partName.length() == 0 || partName.matches("[0-9]*")) {
                success = false;
                nameCheck = false;
                return success;
            }
            //Double checking to make sure inventory logical error is implemented correctly before part creation
                if ((partStock >= partMin) & (partStock <= partMax) && partMin != partMax) {
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
                        if (response == okayButton) {
                            alert.close();
                        } else if (response == cancelButton) {
                            alert.close();
                        }
                    });
                }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Unable to create part");
            alert.setContentText("Please check fields highlights in red to fix errors");
            alert.showAndWait().ifPresent(response -> {
            });
            System.err.println("Error creating part.. Check fields in red");
        }

        System.out.println("SUCCESS = " + success);
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

    /**
     * Highlights errors red and valid data green
     */
    public void presentErrors() {
        if(!nameCheck) {
            addPartNameTF.setStyle("-fx-border-color: #ae0700");
        } else {
            addPartNameTF.setStyle("-fx-border-color: #9f07");
        }
        if(!maxCheck) {
            addPartMaxTF.setStyle("-fx-border-color: #ae0700");
        } else {
            addPartMaxTF.setStyle("-fx-border-color: #9f07");
        }
        if(!minCheck) {
            addPartMinTF.setStyle("-fx-border-color: #ae0700");
        } else {
            addPartMinTF.setStyle("-fx-border-color: #9f07");
        }
        if(!invCheck) {
            addPartInvTF.setStyle("-fx-border-color: #ae0700");
        } else {
            addPartInvTF.setStyle("-fx-border-color: #9f07");
        }
        if(!priceCheck) {
            addPartPriceTF.setStyle("-fx-border-color: #ae0700");
        } else {
            addPartPriceTF.setStyle("-fx-border-color: #9f07");
        }
        if(sourceButton.isSelected()) {
            if(!machineIdCheck) {
                addPartMachineIDCompNameTF.setStyle("-fx-border-color: #ae0700");
            } else {
                addPartMachineIDCompNameTF.setStyle("-fx-border-color: #9f07");
            }
        } else {
            if(!companyNameCheck) {
                addPartMachineIDCompNameTF.setStyle("-fx-border-color: #ae0700");
            } else {
                addPartMachineIDCompNameTF.setStyle("-fx-border-color: #9f07");
            }
        }
    }
}
