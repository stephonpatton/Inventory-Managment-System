package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;

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

import static View_Controller.MainController.*;

public class ModifyPartController implements Initializable {
    //Gets current partID from inventory
    private int partId;

    //Inhouse radio button
    @FXML private RadioButton sourceButton;
    //Outsourced radio button
    @FXML private RadioButton outsourcedButton;
    //Machine ID/Company name label
    @FXML private Label invSourceLabel;
    //Text field for  part ID (Disabled)
    @FXML private TextField modifyPartIDTextField;
    //Text field for part name
    @FXML private TextField modifyPartNameTF;
    //Text field for part inventory
    @FXML private TextField modifyPartInvTF;
    //Text field for part price
    @FXML private TextField modifyPartPriceTF;
    //Text field for part max
    @FXML private TextField modifyPartMaxTF;
    //Text field for part min
    @FXML private TextField modifyPartMinTF;
    //Text field for machine ID/company name
    @FXML private TextField modifyPartMachineCompTF;

    //Gets the index of the part being modified
    int indexOfPart = partIndexToModify();

    //Boolean variables to check if fields have valid data
    private boolean minCheck;
    private boolean maxCheck;
    private boolean priceCheck;
    private boolean invCheck;
    private boolean nameCheck;
    private boolean machineIdCheck;
    private boolean companyNameCheck;

    /**
     * Checks to see which radio button is selected and changes the label value based on it
     */
    public void checkRadioButton() {
        if(sourceButton.isSelected()) {
            invSourceLabel.setText("Machine ID");
        } else {
            invSourceLabel.setText("Company Name");
        }
    }

    /**
     * Returns to the main screen if cancel is pressed on the ModifyPart screen
     * @param actionEvent Cancel button pressed
     */
    public void cancelModifyPart(ActionEvent actionEvent) {
        Parent root;
        try {
            //Setting the scene for the main page
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
     * Creates new part if data has changed
     */
    public boolean createPart() {
        boolean created = false;
        //Checks all fields for errors
        if(checkAllErrors()) {
            try {
                //Tries to get input data
                String partName = modifyPartNameTF.getText();
                double partPrice = Double.parseDouble(modifyPartPriceTF.getText());
                int partStock = Integer.parseInt(modifyPartInvTF.getText());
                int partMax = Integer.parseInt(modifyPartMaxTF.getText());
                int partMin = Integer.parseInt(modifyPartMinTF.getText());

                //Checks which radio button is selected and creates part based on that
                if((partStock >= partMin) & (partStock <= partMax)) {
                if (sourceButton.isSelected()) {
                    if (modifyPartMachineCompTF.getText().matches("[0-9]*")) {
                        int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
                        InhousePart newInhousePart = new InhousePart(partToModify().getId(), partName, partPrice, partStock, partMin, partMax, machineID);
                        Inventory.updatePart(partIndexToModify(), newInhousePart);
                        machineIdCheck = true;
                        created = true;
                    } else {
                        created = false;
                        machineIdCheck = false;
                        System.out.println("Please include numbers only for machine ID");
                    }
                } else if (outsourcedButton.isSelected()) {
                    if (modifyPartMachineCompTF.getText().length() != 0 && !modifyPartMachineCompTF.getText().matches("[0-9]*")) {
                        String compName = modifyPartMachineCompTF.getText();
                        OutsourcedPart outsourcedPart = new OutsourcedPart(partToModify().getId(), partName, partPrice, partStock, partMin, partMax, compName);
                        Inventory.updatePart(partIndexToModify(), outsourcedPart);
                        companyNameCheck = true;
                        created = true;
                    } else {
                        created = false;
                        companyNameCheck = false;
                        System.out.println("Please only include letters");
                    }
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
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Unable to create part");
                alert.setContentText("Please check fields highlighted in red to fix errors");
                alert.showAndWait().ifPresent(response -> {
                });
                System.err.println("Error creating part.. Check fields in red");
            }

        }
        return created;
    }

    /**
     * Checks all fields to see if valid data is provided
     * @return True if valid data is provided
     */
    public boolean checkAllErrors() {
        boolean isValid = false;
        if(sourceButton.isSelected()) {
            if(machineIdCheck && nameCheck && maxCheck && minCheck && invCheck && priceCheck) {
                isValid = true;
            } else {
                isValid = false;
            }
        } else if(outsourcedButton.isSelected()) {
            if(companyNameCheck && nameCheck && maxCheck && minCheck && invCheck && priceCheck) {
                isValid = true;
            } else {
                isValid = false;
            }
        }
        return isValid;
    }


    /**
     * Checks if differences are present between input and part being modified
     * @return True if no differences are present
     */
    public boolean checkDifferences() {
        //Checks if data inputted is different from data already in inventory
        //            createPart();
        return checkPartNameDiff() && checkPartInvDiff() && checkPartPriceDiff() && checkPartMaxDiff()
                && checkPartMinDiff() && checkPartMachineCompDiff();
    }

    /**
     * Checks machine ID and company name field to see if differences are present
     * @return True if no differences
     */
    public boolean checkPartMachineCompDiff() {
        boolean isTheSame = false;
        if(Inventory.getAllParts().get(partIndexToModify()) instanceof Model.OutsourcedPart) {
            if(sourceButton.isSelected()) {
                isTheSame = false;
            } else {
                isTheSame = modifyPartMachineCompTF.getText().equals(((OutsourcedPart)Inventory.getAllParts().get(partIndexToModify())).getCompanyName());
                companyNameCheck = true;
                if(isTheSame) {
                    System.out.println("No differences");
                }
            }
        }
        if(Inventory.getAllParts().get(partIndexToModify()) instanceof Model.InhousePart) {
            if (sourceButton.isSelected()) {
                try {
                    int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
                    if (machineID == ((InhousePart) Inventory.getAllParts().get(partIndexToModify())).getMachineId()) {
                        isTheSame = true;
                        System.out.println("No differences");
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    System.err.println("Please include numbers only for Machine ID");
                }
            }
        }
            return isTheSame;
    }

    /**
     * Initial method called when ModifyPart screen is loaded. Sets data from inventory into the fields for modification
     * @param url Required
     * @param resourceBundle Required
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Gets part being modified
        Part partToModify = Inventory.getAllParts().get(indexOfPart);
        //Gets data from inventory and populates fields
        partId = partToModify.getId();
        modifyPartIDTextField.setText(String.valueOf(partId));
        modifyPartNameTF.setText(partToModify.getName());
        modifyPartInvTF.setText(Integer.toString(partToModify.getStock()));
        modifyPartPriceTF.setText(Double.toString( partToModify.getPrice()));
        modifyPartMaxTF.setText(Integer.toString(partToModify.getMax()));
        modifyPartMinTF.setText(Integer.toString(partToModify.getMin()));
        if(partToModify instanceof InhousePart) {
            modifyPartMachineCompTF.setText(Integer.toString(((InhousePart)Inventory.getAllParts().get(indexOfPart)).getMachineId()));
            invSourceLabel.setText("Machine ID");
            sourceButton.setSelected(true);
        } else {
            modifyPartMachineCompTF.setText(((OutsourcedPart) Inventory.getAllParts().get(indexOfPart)).getCompanyName());
            invSourceLabel.setText("Company Name");
            outsourcedButton.setSelected(true);
        }
    }

    /**
     * Checks if part was created or not and let user know. Returns to main screen
     * @param actionEvent Save button pressed
     */
    public void modifyPartSave(ActionEvent actionEvent) {
        if(checkDifferences()) {
            cancelModifyPart(actionEvent);
        } else if(!checkDifferences()) {
            checkAllFields();
            checkIfInvValid();
            presentErrors();
            if (createPart()) {
                cancelModifyPart(actionEvent);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Unable to create part");
                alert.setContentText("Please check fields highlighted in red to fix errors");
                alert.showAndWait().ifPresent(response -> {

                });
                System.err.println("Error creating part.. Check fields in red");
                setAllChecksToFalse();
            }
        }
    }

    /**
     * Checks if inventory data is logically valid (used for field highlighting as well)
     */
    private void checkIfInvValid() {
        try {
            int min = Integer.parseInt(modifyPartMinTF.getText());
            int max = Integer.parseInt(modifyPartMaxTF.getText());
            int inv = Integer.parseInt(modifyPartInvTF.getText());
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

            if ((inv <= max) && (inv >= min)) {
                invCheck = true;
                maxCheck = true;
                minCheck = true;
            }
        }catch(Exception e) {
            System.err.println("Numbers only for inv data");
        }
        if(!maxCheck || !minCheck || !invCheck) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Numbers only for inv, min, and max");
            alert.setContentText("Please only include numbers for inventory, min, and max");
            alert.showAndWait().ifPresent(response -> {

            });
        }
    }

    /**
     * Checks if part name is different from part name in inventory
     * @return True if no differences
     */
    public boolean checkPartNameDiff() {
        String input = modifyPartNameTF.getText();
        boolean isTheSame;
        if(input.equals(partToModify().getName())) {
            isTheSame = true;
            nameCheck = true;
        } else if(input.matches("[0-9]*") || input.length() == 0){
            nameCheck = false;
            isTheSame = false;
        } else {
            nameCheck = true;
            isTheSame = false;
        }
        return isTheSame;
    }

    /**
     * Checks if part inventory is different from part inventory in inventory
     * @return True if no differences
     */
    public boolean checkPartInvDiff() {
        String input = modifyPartInvTF.getText();
        boolean isTheSame = false;
        int tryInt = 0;
        try {
            tryInt = Integer.parseInt(input.trim());
            invCheck = true;
            isTheSame = tryInt == partToModify().getStock();
        }catch (Exception e) {
            System.err.println("Please provide a number for inventory");
            invCheck = false;
        }

        return isTheSame;
    }

    /**
     * Checks if part price is different from part price in inventory
     * @return True if no differences
     */
    public boolean checkPartPriceDiff() {
        String input = modifyPartPriceTF.getText();
        boolean isTheSame = false;
        double tryDouble = 0;
        try {
            tryDouble = Double.parseDouble(input.trim());
            priceCheck = true;
            isTheSame = tryDouble == partToModify().getPrice();
        }catch(Exception e) {
            System.err.println("Please provide a number for price");
            priceCheck = false;
        }
        return isTheSame;
    }

    /**
     * Checks if part max is different from part max in inventory
     * @return True if no differences
     */
    public boolean checkPartMaxDiff() {
        String input = modifyPartMaxTF.getText();
        boolean isTheSame = false;
        int tryMax;
        try {
            tryMax = Integer.parseInt(input.trim());
            maxCheck = true;
            isTheSame = tryMax == partToModify().getMax();
        }catch(Exception e) {
            System.err.println("Please provide a number for max");
            maxCheck = false;
        }
        return isTheSame;
    }

    /**
     * Checks if part min is different from part min in inventory
     * @return True if no differences
     */
    public boolean checkPartMinDiff() {
        String input = modifyPartMinTF.getText();
        boolean isTheSame = false;
        int tryMin;
        try {
            tryMin = Integer.parseInt(input.trim());
            minCheck = true;
            isTheSame = tryMin == partToModify().getMin();
        }catch(Exception e) {
            System.err.println("Please provide a number for min");
            minCheck = false;
        }
        return isTheSame;
    }

    /**
     * Sets all field checks to false for error handling resetting
     */
    private void setAllChecksToFalse() {
        maxCheck = false;
        minCheck = false;
        priceCheck = false;
        invCheck = false;
        machineIdCheck = false;
        companyNameCheck = false;
        nameCheck = true;
    }

    /**
     * Method that uses all field checking methods in one
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
     * Checks name field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkNameField() {
        String input = modifyPartNameTF.getText();
        if(input.matches(".*\\d.*") || input.length() == 0){
            nameCheck = false;
            System.err.println("Please exclude numbers or empty data fields");
        } else {
            nameCheck = true;
        }
    }

    /**
     * Checks min field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkMinFields() {
        String input = modifyPartMinTF.getText();
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
     * Checks max field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkMaxField() {
        String input = modifyPartMaxTF.getText();
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
     * Checks inventory field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkInvField() {
        String input = modifyPartInvTF.getText();
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
     * Checks price field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkPriceField() {
        String input = modifyPartPriceTF.getText();
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
     * Checks machine ID/company name field to make sure valid data is provided and sets check to true or false for error highlighting
     */
    private void checkMachineIdFields() {
        if(sourceButton.isSelected()) {
            String input = modifyPartMachineCompTF.getText();
            int tryInt = 0;
            try {
                machineIdCheck = true;
                tryInt = Integer.parseInt(input.trim());
            } catch (Exception e) {
                machineIdCheck = false;
                System.err.println("Provide numbers only for machine ID");
            }
        } else if(outsourcedButton.isSelected()){
            String input = modifyPartMachineCompTF.getText();
            if(input.matches("[0-9]*") || input.length() == 0) {
                System.err.println("Please exclude numbers or empty data fields");
                companyNameCheck = false;
            } else {
                companyNameCheck = true;
            }
        }
    }

    /**
     * Displays the errors for input data. Green for valid data and red for invalid data
     */
    public void presentErrors() {
        if(!nameCheck) {
            modifyPartNameTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyPartNameTF.setStyle("-fx-border-color: #9f07");
        }
        if(!maxCheck) {
            modifyPartMaxTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyPartMaxTF.setStyle("-fx-border-color: #9f07");
        }
        if(!minCheck) {
            modifyPartMinTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyPartMinTF.setStyle("-fx-border-color: #9f07");
        }
        if(!invCheck) {
            modifyPartInvTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyPartInvTF.setStyle("-fx-border-color: #9f07");
        }
        if(!priceCheck) {
            modifyPartPriceTF.setStyle("-fx-border-color: #ae0700");
        } else {
            modifyPartPriceTF.setStyle("-fx-border-color: #9f07");
        }
        if(sourceButton.isSelected()) {
            if(!machineIdCheck) {
                modifyPartMachineCompTF.setStyle("-fx-border-color: #ae0700");
            } else {
                modifyPartMachineCompTF.setStyle("-fx-border-color: #9f07");
            }
        } else {
            if(!companyNameCheck) {
                modifyPartMachineCompTF.setStyle("-fx-border-color: #ae0700");
            } else {
                modifyPartMachineCompTF.setStyle("-fx-border-color: #9f07");
            }
        }
    }

}

