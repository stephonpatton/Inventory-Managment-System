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

    //Boolean variables to check if fields have valid data
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
            //TODO: Alert box
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
            companyNameCheck = true;
            return true;
        } else {
            //TODO: Alert box
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
            return true;
        } else {
            //TODO: Alert box
            machineIdCheck = false;
            System.out.println("Please provide numbers only for Machine ID");
            return false;
        }
    }

    private void setAllChecksToFalse() {
        maxCheck = false;
        minCheck = false;
        priceCheck = false;
        invCheck = false;
        machineIdCheck = false;
        companyNameCheck = false;
    }

    private void checkAllFields() {
        checkPriceField();
        checkInvField();
        checkMaxField();
        checkMinFields();
        checkMachineIdFields();
        checkNameField();
    }

    private void checkNameField() {
        String input = addPartNameTF.getText();
        if(input.matches("[0-9]*") || input.length() == 0) {
            nameCheck = false;
        } else {
            nameCheck = true;
        }
    }

    private void checkMinFields() {
        String input = addPartMinTF.getText();
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
        String input = addPartMaxTF.getText();
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
        String input = addPartInvTF.getText();
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
        String input = addPartPriceTF.getText();
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
        }catch(Exception e) {
            System.err.println("Please provide a number for price");
            priceCheck = false;
        }
    }

    private void checkMachineIdFields() {
        if(sourceButton.isSelected()) {
            String input = addPartMachineIDCompNameTF.getText();
            int tryInt = 0;
            try {
                machineIdCheck = true;
                tryInt = Integer.parseInt(input.trim());
            } catch (Exception e) {
                machineIdCheck = false;
                System.err.println("Provide numbers only for machine ID");
            }
        } else if(addPartOutsourcedButton.isSelected()){
            String input = addPartMachineIDCompNameTF.getText();
            if(input.matches("[0-9]*") || input.length() == 0) {
                System.err.println("Please exclude numbers or empty data fields");
                companyNameCheck = false;
            } else {
                companyNameCheck = true;
            }
        }
    }

    /**
     * Checks to make sure data is valid and then calls createPart to save data and closes current window
     * @param actionEvent When the save button is pressed
     */
    public void addPartSubmit(ActionEvent actionEvent) {
//        boolean success = false;
//        if(checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
//        && checkAddPartMin() && checkAddPartSource()) {
//            success = createPart();
//        }
//        if(success) {
//            cancelAddPart(actionEvent);
//        } else {
//            ButtonType okayButton = new ButtonType("Okay");
//            ButtonType cancelButton = new ButtonType("Cancel");
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part not created", okayButton, cancelButton);
//            alert.showAndWait().ifPresent(response -> {
//                if(response == okayButton) {
//                    alert.close();
//                } else if(response == cancelButton) {
//                    alert.close();
//                }
//            });
//        }
        if(checkDifferences()) {
            cancelAddPart(actionEvent);
        } else if(!checkDifferences()) {
            checkAllFields();
            presentErrors();
            if(createPart()) {
                cancelAddPart(actionEvent);
            } else {
                setAllChecksToFalse();
            }
        }

    }

    /**
     * Returns true if no differences
     * Returns false if differences are present
     */
    public boolean checkDifferences() {
        //Checks if data inputted is different from data already in inventory
        //            createPart();
        return checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
                && checkAddPartMin() && checkAddPartCompName();
    }

    /**
     * Creates the part object after all data is submitted and adds it to the ObservableList
     */
    public boolean createPart() {
        boolean success = false;
        try {
            String partName = addPartNameTF.getText();
            double partPrice = Double.parseDouble(addPartPriceTF.getText());
            int partStock = Integer.parseInt(addPartInvTF.getText());
            int partMin = Integer.parseInt(addPartMinTF.getText());
            int partMax = Integer.parseInt(addPartMaxTF.getText());
            String partMachineId = addPartMachineIDCompNameTF.getText();

            if (checkAddPartName() && checkAddPartInv() && checkAddPartPrice() && checkAddPartMax()
                    && checkAddPartMin() && checkAddPartSource()) {
                if ((partStock > partMin) & (partStock <= partMax)) {
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
            }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Unable to create part");
            alert.setContentText("Please check fields highlights in red to fix errors");
            alert.showAndWait().ifPresent(response -> {

            });
            System.err.println("Error creating part.. Check fields in red");
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
