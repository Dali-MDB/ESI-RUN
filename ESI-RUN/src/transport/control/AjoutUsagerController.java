package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.TypeCarte;
import transport.core.Usager;
import transport.core.ReductionImpossibleException;
import transport.ui.MainApp;

import java.time.LocalDate;

public class AjoutUsagerController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField adresseField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private CheckBox mobiliteReduiteCheck;

    @FXML
    private Label typeCarteLabel;

    @FXML
    private void initialize() {
        //set a default value for the date
        dateNaissancePicker.setValue(LocalDate.now().minusYears(30));

        //add listeners to update the type of card displayed
        dateNaissancePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateTypeCarteLabel();
        });

        mobiliteReduiteCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateTypeCarteLabel();
        });

        //initialize the display of the type of card
        updateTypeCarteLabel();
    }

    private void updateTypeCarteLabel() {  //update the type of card displayed
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        boolean mobiliteReduite = mobiliteReduiteCheck.isSelected();

        if (dateNaissance != null) {
            //calculate the age
            int age = LocalDate.now().getYear() - dateNaissance.getYear();

            //determine the type of card
            String typeCarte;
            if (mobiliteReduite) {
                typeCarte = "Solidarité (-50%)";
            } else if (age < 25) {
                typeCarte = "Junior (-30%)";
            } else if (age >= 65) {
                typeCarte = "Senior (-25%)";
            } else {
                typeCarte = "Pas de réduction disponible";
            }

            typeCarteLabel.setText(typeCarte);
        }
    }

    @FXML
    private void ajouterUsager(ActionEvent event) {
        //check that all fields are filled
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                adresseField.getText().isEmpty() || dateNaissancePicker.getValue() == null) { //if a field is empty, show an error message
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        //create a new user
        try {
            Usager nouvelUsager = new Usager(
                    nomField.getText(),
                    prenomField.getText(),
                    adresseField.getText(),
                    dateNaissancePicker.getValue(),
                    mobiliteReduiteCheck.isSelected()
            );

            //add the user to the manager
            MainApp.getGestionnaireTransport().ajouterPersonne(nouvelUsager);

            //check the type of card that the user can get (for information)
            String messageInfo;
            try {
                TypeCarte typeCarte = nouvelUsager.determinerTypeCarteOptimal();
                messageInfo = "The user has been added successfully.\nAvailable card type: " + typeCarte +
                        " (-" + typeCarte.getTauxReduction() + "%)";
            } catch (ReductionImpossibleException e) {
                messageInfo = "The user has been added successfully.\nNo card reduction available.";
            }

            //display a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success");
            alert.setHeaderText(null);
            alert.setContentText(messageInfo);
            alert.showAndWait();

            //close the window
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Error adding", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {  //close the window   
        fermerFenetre();
    }

    private void fermerFenetre() {  //close the window
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {  //show the error message   
        Alert alert = new Alert(Alert.AlertType.ERROR); //show the error message   
        alert.setTitle(titre); //set the title of the alert
        alert.setHeaderText(null); //set the header of the alert
        alert.setContentText(message); //set the content of the alert
        alert.showAndWait(); //show the alert and wait for the user to close it
    }
}