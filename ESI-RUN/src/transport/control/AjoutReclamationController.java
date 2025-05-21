package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*;
import transport.ui.MainApp;

import java.util.Map;

public class AjoutReclamationController {

    @FXML
    private ComboBox<TypeReclamation> typeReclamationCombo;

    @FXML
    private ComboBox<Personne> declarantCombo;

    @FXML
    private TextField stationConcerneeField;  

    @FXML
    private TextArea descriptionArea;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();

        //initialize the combobox with the types of complaint
        typeReclamationCombo.setItems(FXCollections.observableArrayList(TypeReclamation.values()));
        typeReclamationCombo.getSelectionModel().selectFirst();  //select the first item in the combobox

        //initialize the combobox with the people
        declarantCombo.setItems(FXCollections.observableArrayList(gestionnaire.getPersonnes()));
    }

    @FXML
    private void ajouterReclamation(ActionEvent event) {
        //check if all the necessary fields are filled
        if (typeReclamationCombo.getValue() == null || declarantCombo.getValue() == null ||
                stationConcerneeField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {  //if a field is empty, show an error message
            afficherErreur("incomplete fields", "Please fill in all the fields.");
            return;
        }

        String stationConcernee = stationConcerneeField.getText();  //get the station concerned

        //check if the station is already suspended
        if (gestionnaire.isStationSuspendue(stationConcernee)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);  //show the alert message
            alert.setTitle("Station suspended");  //set the title of the alert
            alert.setHeaderText("The station is currently suspended");  //set the header of the alert
            alert.setContentText("The station '" + stationConcernee + "' is already suspended due to numerous complaints. The complaint will still be recorded.");
            alert.showAndWait();  //show the alert and wait for the user to close it
        }

        //create the complaint
        try {
            Reclamation nouvelleReclamation = new Reclamation(
                    typeReclamationCombo.getValue(),
                    descriptionArea.getText(),
                    declarantCombo.getValue(),
                    stationConcernee
            );

            //add the complaint to the manager
            gestionnaire.ajouterReclamation(nouvelleReclamation);

            //check if the station must be suspended due to this new complaint
            boolean nouvellementSuspendue = gestionnaire.isStationSuspendue(stationConcernee);

            //show a confirmation message
            if (nouvellementSuspendue) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert suspension");
                alert.setHeaderText("Station suspended");
                alert.setContentText("The complaint has been recorded successfully.\n" +
                        "ATTENTION: The station '" + stationConcernee + "' has received 3 complaints or more and is now suspended.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);  //show the alert message
                alert.setTitle("Success");  //set the title of the alert
                alert.setHeaderText(null);  //set the header of the alert
                alert.setContentText("The complaint has been recorded successfully.");  //set the content of the alert
                alert.showAndWait();  //show the alert and wait for the user to close it
            }

            // Fermer la fenêtre
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Error adding", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {  //close the window
        fermerFenetre();
    }

    private void fermerFenetre() {   //close the window
        Stage stage = (Stage) typeReclamationCombo.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {  //show the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);  //show the alert message
        alert.setTitle(titre);  //set the title of the alert
        alert.setHeaderText(null);  //set the header of the alert
        alert.setContentText(message);  //set the content of the alert
        alert.showAndWait();  //show the alert and wait for the user to close it
    }
}