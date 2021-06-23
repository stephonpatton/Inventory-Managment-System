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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static View_Controller.MainController.partIndexToModify;
import static View_Controller.MainController.partToModify;

public class ModifyPartController implements Initializable {
    private int partId;

    @FXML private RadioButton sourceButton;
    @FXML private RadioButton outsourcedButton;
    @FXML private Label invSourceLabel;
    @FXML private TextField modifyPartIDTextField;
    @FXML private TextField modifyPartNameTF;
    @FXML private TextField modifyPartInvTF;
    @FXML private TextField modifyPartPriceTF;
    @FXML private TextField modifyPartMaxTF;
    @FXML private TextField modifyPartMinTF;
    @FXML private TextField modifyPartMachineCompTF;

    int indexOfPart = partIndexToModify();

    public void checkRadioButton() {
        if(sourceButton.isSelected()) {
            invSourceLabel.setText("Machine ID");
        } else {
            invSourceLabel.setText("Company Name");
        }
    }

    public void cancelModifyPart(ActionEvent actionEvent) {
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

    public void createPart() {
        String partName = modifyPartNameTF.getText();
        double partPrice = Double.parseDouble(modifyPartPriceTF.getText());
        int partStock = Integer.parseInt(modifyPartInvTF.getText());
        int partMax = Integer.parseInt(modifyPartMaxTF.getText());
        int partMin = Integer.parseInt(modifyPartMinTF.getText());


        if(sourceButton.isSelected()) {
            int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
            InhousePart newInhousePart = new InhousePart(partIndexToModify(), partName, partPrice, partStock, partMin, partMax, machineID);
            Inventory.addPart(newInhousePart);
        }
        else if(outsourcedButton.isSelected()) {
            String compName = modifyPartMachineCompTF.getText();
//            OutsourcedPart outsourcedPart = new OutsourcedPart(partToModify(), partName, partPrice, partStock, partMin, partMax, compName);
            OutsourcedPart outsourcedPart = new OutsourcedPart(partToModify().getId(), partName, partPrice, partStock, partMin, partMax, compName);
//            Inventory.addPart(outsourcedPart);

            Inventory.updatePart(partIndexToModify(), outsourcedPart);
        }
    }

    /**
     * Returns true if no differences
     * Returns false if differences are present
     */
    //TODO: Check differences in each field and compare with data in Observable list
    public boolean checkDifferences() {
        if(checkPartNameDiff() && checkPartInvDiff() && checkPartPriceDiff() && checkPartMaxDiff()
        && checkPartMinDiff() && checkPartMachineCompDiff()) {
            return true;
            //TODO: Means it is the same so just hide screen upon save button click
        } else {
//            Inventory.deletePart(Inventory.getAllParts().get(partIndexToModify()));
            createPart();
            //TODO: Write data to new part object, delete existing part, -1 from product index, add part to list (preferably at same index), add 1 to list
            return false;
        }
    }

    public boolean checkPartMachineCompDiff() {
        boolean isTheSame = false;
        if(sourceButton.isSelected()) {
            int machineID = Integer.parseInt(modifyPartMachineCompTF.getText());
            if(machineID == ((InhousePart)Inventory.getAllParts().get(partIndexToModify())).getMachineId()) {
                isTheSame = true;
                System.out.println("No differences");
            }
        } else if(outsourcedButton.isSelected()) {
            isTheSame = modifyPartMachineCompTF.getText().equals(((OutsourcedPart)Inventory.getAllParts().get(partIndexToModify())).getCompanyName());
            if(isTheSame) {
                System.out.println("No differences");
            }
        }
            return isTheSame;
    }

    //TODO: Submit functionality (include checkdiff method)

    //TODO: Figure out update method and how to overwrite vs delete and add again

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part partToModify = Inventory.getAllParts().get(indexOfPart);
        partId = partToModify.getId();
        modifyPartIDTextField.setText("AUTO GEN: " + partId);
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

    public void modifyPartSave(ActionEvent actionEvent) {
        if (!checkDifferences()) {
            System.out.println("Created part");
        }
        cancelModifyPart(actionEvent);
    }

    public boolean checkPartNameDiff() {
        return modifyPartNameTF.getText().equals(Inventory.getAllParts().get(partIndexToModify()).getName());
    }

    public boolean checkPartInvDiff() {
        int stock = Integer.parseInt(modifyPartInvTF.getText());
        return stock == Inventory.getAllParts().get(partIndexToModify()).getStock();
    }

    public boolean checkPartPriceDiff() {
        double price = Double.parseDouble(modifyPartPriceTF.getText());
        return price == Inventory.getAllParts().get(partIndexToModify()).getPrice();
    }

    public boolean checkPartMaxDiff() {
        int max = Integer.parseInt(modifyPartMaxTF.getText());
        return max == Inventory.getAllParts().get(partIndexToModify()).getMax();
    }

    public boolean checkPartMinDiff() {
        int min = Integer.parseInt(modifyPartMinTF.getText());
        return min == Inventory.getAllParts().get(partIndexToModify()).getMin();
    }
}

