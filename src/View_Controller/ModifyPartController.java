package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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

    int indexOfPart = partToModify();

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

    //TODO: Check differences in each field and compare with data in Observable list
    public void checkDifferences() {
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
        if(partToModify instanceof InHouse) {
            modifyPartMachineCompTF.setText(Integer.toString(((InHouse)Inventory.getAllParts().get(indexOfPart)).getMachineId()));
            invSourceLabel.setText("Machine ID");
            sourceButton.setSelected(true);
        } else {
            modifyPartMachineCompTF.setText(((Outsourced) Inventory.getAllParts().get(indexOfPart)).getCompanyName());
            invSourceLabel.setText("Company Name");
            outsourcedButton.setSelected(true);
        }
    }
}
