package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.Employe;
import transport.core.TypeFonction;
import transport.ui.MainApp;

import java.time.LocalDate;

public class AjoutEmployeController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField adresseField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private TextField matriculeField;

    @FXML
    private ComboBox<TypeFonction> fonctionCombo;

    @FXML
    private CheckBox mobiliteReduiteCheck;

    @FXML
    private void initialize() {
        //initialize the combobox with the types of function
        fonctionCombo.setItems(FXCollections.observableArrayList(TypeFonction.values()));
        fonctionCombo.getSelectionModel().selectFirst(); //select the first item in the combobox

        //define a default value for the date
        dateNaissancePicker.setValue(LocalDate.now().minusYears(25));
    }

    @FXML
    private void ajouterEmploye(ActionEvent event) {
        //check if all fields are filled
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                adresseField.getText().isEmpty() || dateNaissancePicker.getValue() == null ||
                matriculeField.getText().isEmpty() || fonctionCombo.getValue() == null) { //if a field is empty, show an error message
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        
        try { //try to create a new employee
            Employe nouvelEmploye = new Employe(
                    nomField.getText(),
                    prenomField.getText(),
                    adresseField.getText(),
                    dateNaissancePicker.getValue(),
                    mobiliteReduiteCheck.isSelected(),
                    matriculeField.getText(),
                    fonctionCombo.getValue()
            );

            //add the employee to the manager
            MainApp.getGestionnaireTransport().ajouterPersonne(nouvelEmploye);

            //show a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);  //display the alert message
            alert.setTitle("Success");  //set the title of the alert
            alert.setHeaderText(null);  //set the header of the alert
            alert.setContentText("The employee has been added successfully.");  //set the content of the alert
            alert.showAndWait();  //show the alert and wait for the user to close it

            //close the window
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Error adding employee", "An error occurred: " + e.getMessage());
        } //+ the error message is displayed in the alert
    }

    @FXML
    private void annuler(ActionEvent event) {   //the page to go back to the main page
        fermerFenetre();
    }

    private void fermerFenetre() {  //close the window
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {  //display the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);   //show the alert message
        alert.setTitle(titre);   //set the title of the alert
        alert.setHeaderText(null);  //set the header of the alert
        alert.setContentText(message);  //set the content of the alert  
        alert.showAndWait();  //show the alert and wait for the user to close it
    }
}