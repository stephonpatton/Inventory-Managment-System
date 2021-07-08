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

import static View_Controller.MainController.partIndexToModify;
import static View_Controller.MainController.partToModify;

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
    public void createPart() {
        //Gets input data
        String partName = modifyPartNameTF.getText();
        double partPrice = Double.parseDouble(modifyPartPriceTF.getText());
        int partStock = Integer.parseInt(modifyPartInvTF.getText());
        int partMax = Integer.parseInt(modifyPartMaxTF.getText());
        int partMin = Integer.parseInt(modifyPartMinTF.getText());

        //Checks which radio button is selected and creates part based on that
        if(sourceButton.isSelected()) {
            if(modifyPartMachineCompTF.getText().matches("[0-9]*")) {
                int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
                InhousePart newInhousePart = new InhousePart(partToModify().getId(), partName, partPrice, partStock, partMin, partMax, machineID);
                Inventory.updatePart(partIndexToModify(), newInhousePart);
            } else {
                System.out.println("Please include numbers only for machine ID");
            }
        }
        else if(outsourcedButton.isSelected()) {
            if(modifyPartMachineCompTF.getText().length() != 0 && !modifyPartMachineCompTF.getText().matches("[0-9]*")) {
                String compName = modifyPartMachineCompTF.getText();
                OutsourcedPart outsourcedPart = new OutsourcedPart(partToModify().getId(), partName, partPrice, partStock, partMin, partMax, compName);
                Inventory.updatePart(partIndexToModify(), outsourcedPart);
            } else {
                System.out.println("Please only include letters");
            }
        }
    }

    /**
     * Returns true if no differences
     * Returns false if differences are present
     */
    public boolean checkDifferences() {
        //Checks if data inputted is different from data already in inventory
        if(checkPartNameDiff() && checkPartInvDiff() && checkPartPriceDiff() && checkPartMaxDiff()
        && checkPartMinDiff() && checkPartMachineCompDiff()) {
            return true;
        } else {
            createPart();
            return false;
        }
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
                if(isTheSame) {
                    System.out.println("No differences");
                }
            }
        }
        if(Inventory.getAllParts().get(partIndexToModify()) instanceof Model.InhousePart) {
            if(sourceButton.isSelected()) {
                int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
                if(machineID == ((InhousePart)Inventory.getAllParts().get(partIndexToModify())).getMachineId()) {
                    isTheSame = true;
                    System.out.println("No differences");
                }
            } else {
                return false;
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
        if (!checkDifferences()) {
            System.out.println("Created part");
        }
        cancelModifyPart(actionEvent);
    }

    /**
     * Checks if part name is different from part name in inventory
     * @return True if no differences
     */
    public boolean checkPartNameDiff() {
        return modifyPartNameTF.getText().equals(Inventory.getAllParts().get(partIndexToModify()).getName());
    }

    /**
     * Checks if part inventory is different from part inventory in inventory
     * @return True if no differences
     */
    public boolean checkPartInvDiff() {
        int stock = Integer.parseInt(modifyPartInvTF.getText());
        return stock == Inventory.getAllParts().get(partIndexToModify()).getStock();
    }

    /**
     * Checks if part price is different from part price in inventory
     * @return True if no differences
     */
    public boolean checkPartPriceDiff() {
        double price = Double.parseDouble(modifyPartPriceTF.getText());
        return price == Inventory.getAllParts().get(partIndexToModify()).getPrice();
    }

    /**
     * Checks if part max is different from part max in inventory
     * @return True if no differences
     */
    public boolean checkPartMaxDiff() {
        int max = Integer.parseInt(modifyPartMaxTF.getText());
        return max == Inventory.getAllParts().get(partIndexToModify()).getMax();
    }

    /**
     * Checks if part min is different from part min in inventory
     * @return True if no differences
     */
    public boolean checkPartMinDiff() {
        int min = Integer.parseInt(modifyPartMinTF.getText());
        return min == Inventory.getAllParts().get(partIndexToModify()).getMin();
    }
}

